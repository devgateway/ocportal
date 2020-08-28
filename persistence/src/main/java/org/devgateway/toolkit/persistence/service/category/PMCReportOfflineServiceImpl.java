package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.StatusChangedComment;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.PMCMember;
import org.devgateway.toolkit.persistence.dao.form.PMCNotes;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dto.PMCMemberOffline;
import org.devgateway.toolkit.persistence.dto.PMCNotesOffline;
import org.devgateway.toolkit.persistence.dto.PMCReportOffline;
import org.devgateway.toolkit.persistence.dto.StatusChangedCommentOffline;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class PMCReportOfflineServiceImpl implements PMCReportOfflineService {

    @Autowired
    private PMCReportService pmcReportService;

    @Autowired
    private PersonService personService;

    protected PMCNotesOffline convertToOffline(PMCNotes n) {
        PMCNotesOffline no = new PMCNotesOffline();
        no.setId(n.getId());
        no.setText(n.getText());
        return no;
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


    protected PMCReportOffline convertToOffline(PMCReport pmc) {
        PMCReportOffline pmco = new PMCReportOffline();
        pmco.setId(pmc.getId());
        pmco.setAcknowledgeSignature(pmc.getAcknowledgeSignature());
        pmco.setAuthorizePayment(pmc.getAuthorizePayment());
        pmco.setSignatureNames(pmc.getSignatureNames());
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
        Person user = loadObjectById(userId, personService);
        return pmcReportService.getPMCReportsForDepartments(
                user.getDepartments()).stream().map(this::convertToOffline).collect(Collectors.toList());
    }
}
