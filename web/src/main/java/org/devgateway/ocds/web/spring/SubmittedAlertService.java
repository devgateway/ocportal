package org.devgateway.ocds.web.spring;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.util.Strings;
import org.devgateway.ocds.web.util.SettingsUtils;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.hibernate.proxy.HibernateProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class SubmittedAlertService {

    protected static Logger logger = LoggerFactory.getLogger(SubmittedAlertService.class);

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SettingsUtils settingsUtils;

    @Autowired
    private AwardAcceptanceService awardAcceptanceService;

    @Autowired
    private AwardNotificationService awardNotificationService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProfessionalOpinionService professionalOpinionService;

    @Autowired
    private PurchaseRequisitionService purchaseRequisitionService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private TenderQuotationEvaluationService tenderQuotationEvaluationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${serverURL}")
    private String serverURL;

    private List<? extends AbstractMakueniEntityService> services;

    @PostConstruct
    public void init() {

        services = Collections.unmodifiableList(Arrays.asList(
                projectService, //not pr
                awardAcceptanceService,
                awardNotificationService,
                contractService,
                procurementPlanService, //not pr
                professionalOpinionService,
                purchaseRequisitionService, //not pr
                tenderService,
                tenderQuotationEvaluationService));
    }

    @PreDestroy
    public void destroy() {
        services = null;
    }

    public Set<String> getValidatorEmailsForDepartment(Long departmentId) {
        Optional<Department> department = departmentService.findById(departmentId);
        return department.map(value -> personService.findByRoleIn(SecurityConstants.Roles.ROLE_VALIDATOR)
                .stream().map(Person::getEmail).map(Strings::trimToNull).filter(Objects::nonNull)
                .collect(Collectors.toSet())).orElseGet(Sets::newHashSet);
    }

    private String createDepartmentContent(Long department, Map<Class<? extends AbstractMakueniEntity>,
            Map<Long, String[]>> notifyMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("The following forms are pending your approval:<br/>");
        sb.append("<ul>");
        notifyMap.forEach((aClass, longStringMap) -> {
            sb.append("<li>").append(aClass.getSimpleName()).append("<ul>");
            longStringMap.forEach((aLong, s) -> {
                sb.append("<li>").append(getLinkedEntityWithTitle(aClass, s[0], s[1], aLong)).append("</li>");
            });
            sb.append("</ul></li>");
        });
        sb.append("</ul>");
        return sb.toString();
    }

    private void sendDepartmentEmails(Long department,
                                      Map<Class<? extends AbstractMakueniEntity>, Map<Long, String[]>> notifyMap) {
        String departmentName = departmentService.findById(department).get().getLabel();
        String[] strings = getValidatorEmailsForDepartment(department).toArray(new String[0]);
        if (strings.length == 0 || notifyMap.isEmpty()) {
            return;
        }
        final MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, "UTF-8");
            msg.setTo(strings);
            msg.setFrom("noreply@dgstg.org");
            msg.setSubject("Reminder - There are pending forms for you to validate on department " + departmentName);
            msg.setText(createDepartmentContent(department, notifyMap), true);
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error("Failed to send validator notification email for: " + strings, e);
            throw e;
        }
    }


    public Map<Long, Map<Class<? extends AbstractMakueniEntity>, Map<Long, String[]>>> collectDepartmentTypeIdTitle() {
        Map<Long, Map<Class<? extends AbstractMakueniEntity>, Map<Long, String[]>>> departmentTypeIdTitle =
                new ConcurrentHashMap<>();

        Integer daysSubmittedReminder = settingsUtils.getDaysSubmittedReminder();

        try (Stream<? extends AbstractMakueniEntity> allSubmitted = services.stream()
                .flatMap(AbstractMakueniEntityService::getAllSubmitted)) {
            allSubmitted
                    .filter(e -> Duration.between(e.getLastModifiedDate().get().toLocalDate().atStartOfDay(),
                            LocalDate.now().atStartOfDay()).toDays() >= daysSubmittedReminder)
                    .forEach(
                    o -> {
                        AbstractMakueniEntity e = (AbstractMakueniEntity) o;
                        Department department = e.getDepartment();
                        departmentTypeIdTitle.putIfAbsent(department.getId(), new ConcurrentHashMap<>());
                        Map<Class<? extends AbstractMakueniEntity>, Map<Long, String[]>> departmentMap =
                                departmentTypeIdTitle
                                        .get(department.getId());
                        Class clazz = HibernateProxyHelper.getClassWithoutInitializingProxy(e);
                        departmentMap.putIfAbsent(clazz, new ConcurrentHashMap<>());
                        Map<Long, String[]> typeMap = departmentMap.get(clazz);
                        typeMap.put(e.getId(), new String[]{e.getLabel(),
                                e.getLastModifiedDate().get().format(
                                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                                )});
                    }
            );
            return departmentTypeIdTitle;
        }
    }

    @Scheduled(cron = "0 0 23 * * SUN")
    @Async
    public void sendNotificationEmails() {
        Map<Long, Map<Class<? extends AbstractMakueniEntity>, Map<Long, String[]>>> notifyMap =
                collectDepartmentTypeIdTitle();
        notifyMap.forEach(this::sendDepartmentEmails);
    }

    private String getLinkedEntityWithTitle(Class clazz, String title, String date, Long id) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"").append(getEntityURL(clazz, id)).append("\">").append(title)
                .append("</a>").append(" pending your validation since ").append(date);
        return sb.toString();
    }

    private String getEntityURL(Class clazz, Long id) {
        return URI.create(serverURL + "/Edit" + clazz.getSimpleName() + "Page/?id=" + id).toASCIIString();
    }


}

