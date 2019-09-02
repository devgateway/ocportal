package org.devgateway.ocds.web.spring;

import com.google.common.collect.Sets;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.ReleaseFlags;
import org.devgateway.ocds.persistence.mongo.repository.main.FlaggedReleaseRepository;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.flags.ReleaseFlagHistory;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.ReleaseFlagHistoryService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.devgateway.ocds.persistence.mongo.flags.FlagsConstants.FLAGS_LIST;

@Service
public class ReleaseFlagNotificationService {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PersonService personService;

    private Map<String, Map<String, Set<String>>> departmentFlagRelease;


    @Autowired
    private ReleaseFlagHistoryService releaseFlagHistoryService;

    @Autowired
    private FlaggedReleaseRepository flaggedReleaseRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TranslationService translationService;

    @PostConstruct
    public void init() {
        departmentFlagRelease = new ConcurrentHashMap<>();
    }


    private void addDepartmentFlagReleaseId(String department, String flag, String releaseId) {
        departmentFlagRelease.putIfAbsent(department, new ConcurrentHashMap<>());
        Map<String, Set<String>> stringSetMap = departmentFlagRelease.get(department);
        stringSetMap.putIfAbsent(flag, new HashSet<>());
        Set<String> set = stringSetMap.get(flag);
        set.add(releaseId);
    }


    @Transactional
    private String getDepartmentNameFromRelease(FlaggedRelease flaggedRelease) {
        Optional<Department> department = departmentService.findById(flaggedRelease.getDepartmentId());
        if (!department.isPresent()) {
            return "Unknown";
        }
        return department.get().getLabel();
    }

    private Set<String> getUsersValidatorsEmailsFromRelease(FlaggedRelease flaggedRelease) {
        Optional<Department> department = departmentService.findById(flaggedRelease.getDepartmentId());
        if (!department.isPresent()) {
            return Sets.newHashSet();
        }

        return personService.findByDepartmentWithRoles(department.get(), SecurityConstants.Roles.ROLE_USER,
                SecurityConstants.Roles.ROLE_VALIDATOR
        ).stream().map(Person::getEmail).collect(Collectors.toSet());
    }

    private Set<String> getAdminsEmails() {
        return personService.findByRoleIn(SecurityConstants.Roles.ROLE_ADMIN).stream().map(Person::getEmail)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addFlaggedReleaseToNotificationTree(FlaggedRelease flaggedRelease) {
        Optional<ReleaseFlagHistory> latestReleaseFlagHistory = releaseFlagHistoryService.findLatestReleaseFlagHistory(
                flaggedRelease.getId());

        FLAGS_LIST.stream().map(ReleaseFlags::getShortFlagName).forEach(f -> {
                    boolean flag = flaggedRelease.getFlags().getFlagSet(f);
                    if (flag && (!latestReleaseFlagHistory.isPresent()
                            || !latestReleaseFlagHistory.get().getFlagged().contains(f))) {
                        addDepartmentFlagReleaseId(
                                getDepartmentNameFromRelease(flaggedRelease),
                                f, flaggedRelease.getId()
                        );

                    }
                }
        );
    }

    public String createAdminContent() {
        StringBuffer sb = new StringBuffer();
        departmentFlagRelease.forEach((department, flagReleaseId) -> {
            sb.append(department).append("<br/>");
            flagReleaseId.forEach((flag, releaseId) -> {
                sb.append(getFlagNameFromId(flag)).append("<br/>");
                releaseId.forEach(r -> sb.append(getReleaseTitle(r)).append("<br/>"));
            });
        });
        return sb.toString();
    }

    public String getFlagNameFromId(String flagName) {
        return translationService.getValue("en_US", "crd:indicators:" + flagName + ":name");
    }

//    public void createAdminEmail() {
//
//        final MimeMessagePreparator messagePreparator = mimeMessage -> {
//            final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage);
//
//            msg.setTo(getAdminsEmails().toArray(new String[0]));
//            msg.setFrom("noreply@dgstg.org");
//            msg.setSubject("# of new Corruption Risk Flags");
//            msg.setText(content.replaceAll("\n", "<br />"), true);
//        };
//        try {
//            javaMailSender.send(messagePreparator);
//        } catch (MailException e) {
//            logger.error("Failed to send verification email for: " + alert.getEmail(), e);
//            throw e;
//        }
//    }

    public String getReleaseTitle(String releaseId) {
        Optional<FlaggedRelease> optRelease = flaggedReleaseRepository.findById(releaseId);
        if (!optRelease.isPresent() || ObjectUtils.isEmpty(optRelease.get().getTender())
                || ObjectUtils.isEmpty(optRelease.get().getTender().getTitle())) {
            return "Unkown [id=" + releaseId + "]";
        }
        return optRelease.get().getTender().getTitle();
    }


    public void sendNotifications() {
        System.out.println("Sending Red Flag Notifications " + departmentFlagRelease);
        System.out.println(createAdminContent());

        departmentFlagRelease.clear();
    }
}
