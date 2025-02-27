package org.devgateway.toolkit.persistence.service.category;

import org.apache.commons.lang3.BooleanUtils;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.StatusChangedComment;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.PMCMember;
import org.devgateway.toolkit.persistence.dao.form.PMCNotes;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dto.PMCMemberOffline;
import org.devgateway.toolkit.persistence.dto.PMCNotesOffline;
import org.devgateway.toolkit.persistence.dto.PMCReportOffline;
import org.devgateway.toolkit.persistence.dto.StatusChangedCommentOffline;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.persistence.validator.groups.Draft;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
@Validated
public class PMCReportOfflineServiceImpl implements PMCReportOfflineService {

    @Autowired
    private PMCReportService pmcReportService;

    @Autowired
    private PersonService personService;

    @Autowired
    private WardService wardService;

    @Autowired
    private SubcountyService subcountyService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private PMCStatusService pmcStatusService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private PMCStaffService staffService;

    @Autowired
    private ProjectClosureHandoverService projectClosureHandoverService;

    protected PMCNotesOffline convertToOffline(PMCNotes n) {
        PMCNotesOffline no = new PMCNotesOffline();
        no.setId(n.getId());
        no.setText(n.getText());
        return no;
    }


    protected <C extends AbstractAuditableEntity> C getChildEntity(
            Long childId, List<C> childList) {
        if (childId == null) {
            return null;
        }
        Optional<C> first = childList.stream().filter(n -> n.getId().equals(childId)).findFirst();
        return first.orElse(null);
    }


    protected PMCNotes convertToDao(PMCNotesOffline no, PMCReport parentDao) {
        PMCNotes notes = getChildEntity(no.getId(), parentDao.getPmcNotes());
        if (notes == null) {
            notes = new PMCNotes();
        }
        notes.setParent(parentDao);
        notes.setText(no.getText());
        return notes;
    }

    protected StatusChangedComment convertToDao(StatusChangedCommentOffline off, PMCReport parentDao) {
        StatusChangedComment comment = getChildEntity(off.getId(), parentDao.getStatusComments());
        if (comment == null) {
            comment = new StatusChangedComment();
        }
        comment.setComment(off.getComment());
        comment.setStatus(off.getStatus());
        comment.setCreatedBy(off.getCreatedBy());
        comment.setCreatedDate(off.getCreatedDate());
        return comment;
    }


    protected PMCMember convertToDao(PMCMemberOffline mo, PMCReport parentDao) {
        PMCMember ret = getChildEntity(mo.getId(), parentDao.getPmcMembers());
        if (ret == null) {
            ret = new PMCMember();
        }
        ret.setParent(parentDao);
        if (mo.getDesignationId() != null) {
            ret.setDesignation(loadObjectById(mo.getDesignationId(), designationService));
        }
        if (mo.getStaffId() != null) {
            ret.setStaff(loadObjectById(mo.getStaffId(), staffService));
        }
        return ret;
    }

    protected StatusChangedCommentOffline convertToOffline(StatusChangedComment c) {
        StatusChangedCommentOffline co = new StatusChangedCommentOffline();
        co.setId(c.getId());
        co.setComment(c.getComment());
        co.setCreatedBy(c.getCreatedBy().get());
        co.setCreatedDate(c.getCreatedDate().get());
        co.setStatus(c.getStatus());
        return co;
    }

    protected PMCMemberOffline convertToOffline(PMCMember m) {
        PMCMemberOffline mo = new PMCMemberOffline();
        mo.setId(m.getId());
        if (m.getDesignation() != null) {
            mo.setDesignationId(m.getDesignation().getId());
        }
        if (m.getStaff() != null) {
            mo.setStaffId(m.getStaff().getId());
        }
        return mo;
    }

    protected <T extends AbstractAuditableEntity> T loadObjectById(Long id, BaseJpaService<T> service) {
        Optional<T> byId = service.findById(id);
        if (!byId.isPresent()) {
            throw new RuntimeException("Cannot find object with id " + id + " of service "
                    + service.getClass().getSimpleName());
        }
        return byId.get();
    }

    public Person loadPersonById(Long id, PersonService service) {
        Person person = loadObjectById(id, personService);
        if (BooleanUtils.isFalse(person.isEnabled())) {
            throw new RuntimeException("User with id " + id + " is disabled");
        }
        return person;
    }

    protected <C extends AbstractAuditableEntity> void addToList(Supplier<List<C>> getter, Consumer<List<C>> setter,
                                                                 Stream<C> inStream) {
        if (getter.get() == null) {
            setter.accept(inStream.collect(Collectors.toList()));
        } else {
            getter.get().clear();
            getter.get().addAll(inStream.collect(Collectors.toList()));
        }
    }

    @Validated(NonDraft.class)
    @Override
    public PMCReport convertToDaoNonDraft(@Valid PMCReportOffline pmco, Person person) {
        return convertToDao(pmco, person);
    }

    @Validated(Draft.class)
    @Override
    public PMCReport convertToDaoDraft(@Valid PMCReportOffline pmco, Person person) {
        return convertToDao(pmco, person);
    }


    public PMCReport convertToDao(PMCReportOffline pmco, Person person) {
        PMCReport pmc;
        if (pmco.getId() != null) {
            pmc = loadObjectById(pmco.getId(), pmcReportService);
            pmc.setLastModifiedBy(person.getUsername());
            pmc.setLastModifiedDate(ZonedDateTime.now());
        } else {
            pmc = pmcReportService.newInstance();
            Tender tender = loadObjectById(pmco.getTenderId(), tenderService);
            pmc.setTenderProcess(tender.getTenderProcess());
            if (ObjectUtils.isEmpty(tender.getTenderProcess().getContract())
                    || !DBConstants.Status.APPROVED.equals(tender.getTenderProcess().getSingleContract().getStatus())) {
                throw new RuntimeException("Cannot create a PMC Report inside a Tender Process with no "
                        + "approved Contract");
            }
            pmc.setCreatedBy(person.getUsername());
            pmc.setCreatedDate(ZonedDateTime.now());
        }
        pmc.setAcknowledgeSignature(pmco.getAcknowledgeSignature());
        pmc.setAuthorizePayment(pmco.getAuthorizePayment());
        pmc.setSocialSafeguards(pmco.getSocialSafeguards());
        pmc.setEmergingComplaints(pmco.getEmergingComplaints());
        pmc.setPmcChallenges(pmco.getPmcChallenges());
        pmc.setSignatureNames(pmco.getSignatureNames());
        if (pmco.getPmcStatusId() != null) {
            pmc.setPmcStatus(loadObjectById(pmco.getPmcStatusId(), pmcStatusService));
        }

        addToList(pmc::getWards, pmc::setWards, pmco.getWardIds().stream().map(id -> loadObjectById(id, wardService)));

        addToList(pmc::getSubcounties, pmc::setSubcounties, pmco.getSubcountyIds().stream().map(id -> loadObjectById(id,
                subcountyService)));

        addToList(pmc::getPmcNotes, pmc::setPmcNotes, pmco.getPmcNotes().stream().map(n -> convertToDao(n, pmc)));

        addToList(pmc::getStatusComments, pmc::setStatusComments, pmco.getStatusComments().stream().map(n ->
                convertToDao(n, pmc)));

        addToList(pmc::getPmcMembers, pmc::setPmcMembers, pmco.getPmcMembers().stream().map(n -> convertToDao(n, pmc)));

        addToList(pmc::getProjectClosureHandover, pmc::setProjectClosureHandover,
                pmco.getProjectClosureHandoverIds().stream().map(c -> loadObjectById(c,
                        projectClosureHandoverService)));

        pmc.setApprovedDate(pmco.getReportDate());
        pmc.setStatus(pmco.getStatus());

        return pmc;
    }

    @Override
    public PMCReportOffline convertToOffline(PMCReport pmc) {
        PMCReportOffline pmco = new PMCReportOffline();
        pmco.setId(pmc.getId());
        pmco.setLastModifiedDate(pmc.getLastModifiedDate().orElse(null));
        pmco.setCreatedDate(pmc.getCreatedDate().orElse(null));
        pmco.setAcknowledgeSignature(pmc.getAcknowledgeSignature());
        pmco.setAuthorizePayment(pmc.getAuthorizePayment());
        pmco.setSocialSafeguards(pmc.getSocialSafeguards());
        pmco.setEmergingComplaints(pmc.getEmergingComplaints());
        pmco.setPmcChallenges(pmc.getPmcChallenges());
        pmco.setSignatureNames(pmc.getSignatureNames());
        pmco.setRejected(pmc.getRejected());
        pmco.setPmcMembers(pmc.getPmcMembers().stream().map(this::convertToOffline).collect(Collectors.toList()));
        pmco.setPmcNotes(pmc.getPmcNotes().stream().map(this::convertToOffline).collect(Collectors.toList()));
        pmco.setStatusComments(pmc.getStatusComments().stream().map(this::convertToOffline)
                .collect(Collectors.toList()));
        if (pmc.getPmcStatus() != null) {
            pmco.setPmcStatusId(pmc.getPmcStatus().getId());
        }
        pmco.setWardIds(pmc.getWards().stream().map(Ward::getId).collect(Collectors.toSet()));
        pmco.setSubcountyIds(pmc.getSubcounties().stream().map(Subcounty::getId).collect(Collectors.toSet()));
        pmco.setProjectClosureHandoverIds(pmc.getProjectClosureHandover().stream().map(ProjectClosureHandover::getId)
                .collect(Collectors.toSet()));
        pmco.setTenderId(pmc.getTenderProcess().getSingleTender().getId());
        pmco.setStatus(pmc.getStatus());
        pmco.setReportDate(pmc.getApprovedDate());

        return pmco;
    }

    @Override
    public List<PMCReportOffline> getPMCReports(Long userId) {
        Person user = loadPersonById(userId, personService);
        return pmcReportService.getPMCReportsCreatedBy(user.getUsername(), user.getDepartments()).stream()
                .map(this::convertToOffline)
                .collect(Collectors.toList());
    }

}

