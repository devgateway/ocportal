package org.devgateway.toolkit.persistence.validator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinionItem;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.test.AbstractPersistenceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.APPROVED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Profile("integration")
public class TenderProcessValidatorTest extends AbstractPersistenceTest {

    @Autowired
    private TenderProcessService tenderProcessService;

    private Tender tender(TenderProcess tp, String status) {
        Tender t = mock(Tender.class);
        when(t.getTenderProcess()).thenReturn(tp);
        when(t.getStatus()).thenReturn(status);
        when(tp.getSingleTender()).thenReturn(t);
        when(tp.getTender()).thenReturn(ImmutableSet.of(t));
        return t;
    }

    private AwardAcceptance awardAcceptance(TenderProcess tp, String status) {
        AwardAcceptance aa = mock(AwardAcceptance.class);
        when(aa.getTenderProcess()).thenReturn(tp);
        when(aa.getStatus()).thenReturn(status);
        when(tp.getAwardAcceptance()).thenReturn(ImmutableSet.of(aa));
        when(tp.getSingleAwardAcceptance()).thenReturn(aa);
        return aa;
    }

    private ProfessionalOpinion professionalOpinion(TenderProcess tp, String status) {
        ProfessionalOpinion po = mock(ProfessionalOpinion.class);
        when(po.getTenderProcess()).thenReturn(tp);
        when(po.getStatus()).thenReturn(status);
        when(tp.getProfessionalOpinion()).thenReturn(ImmutableSet.of(po));
        when(tp.getSingleProfessionalOpinion()).thenReturn(po);
        return po;
    }

    private TenderQuotationEvaluation quotationEvaluation(TenderProcess tp, String status) {
        TenderQuotationEvaluation r = mock(TenderQuotationEvaluation.class);
        when(r.getTenderProcess()).thenReturn(tp);
        when(r.getStatus()).thenReturn(status);
        when(tp.getTenderQuotationEvaluation()).thenReturn(ImmutableSet.of(r));
        when(tp.getSingleTenderQuotationEvaluation()).thenReturn(r);
        return r;
    }

    private Contract contract(TenderProcess tp, String status) {
        Contract c = mock(Contract.class);
        when(c.getTenderProcess()).thenReturn(tp);
        when(c.getStatus()).thenReturn(status);
        when(tp.getSingleContract()).thenReturn(c);
        when(tp.getContract()).thenReturn(ImmutableSet.of(c));
        return c;
    }

    private AwardNotification awardNotification(TenderProcess tp, String status) {
        AwardNotification r = mock(AwardNotification.class);
        when(r.getTenderProcess()).thenReturn(tp);
        when(r.getStatus()).thenReturn(status);
        when(tp.getAwardNotification()).thenReturn(ImmutableSet.of(r));
        when(tp.getSingleAwardNotification()).thenReturn(r);
        return r;
    }


    private Project project(TenderProcess tp, String status) {
        Project p = mock(Project.class);
        when(p.getStatus()).thenReturn(status);
        when(tp.getProject()).thenReturn(p);
        return p;
    }

    @Test
    public void testCrossCounts() {
        TenderProcess tp = mock(TenderProcess.class);
        ProfessionalOpinion po = professionalOpinion(tp, APPROVED);
        ProfessionalOpinionItem pi = mock(ProfessionalOpinionItem.class);
        when(po.getItems()).thenReturn(ImmutableList.of(pi));

        AwardNotification an = awardNotification(tp, APPROVED);
        AwardNotificationItem ani = mock(AwardNotificationItem.class);
        when(an.getItems()).thenReturn(ImmutableList.of(ani));

        AwardAcceptance aa = awardAcceptance(tp, APPROVED);
        AwardAcceptanceItem aai = mock(AwardAcceptanceItem.class);
        when(aa.getItems()).thenReturn(ImmutableList.of(aai));

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(0));

        when(an.getItems()).thenReturn(ImmutableList.of());

        validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(2));
    }

    @Test
    public void testTenderAwardNotificationDates() {
        LocalDate date = LocalDate.now();
        TenderProcess tp = mock(TenderProcess.class);
        Tender tender = tender(tp, APPROVED);
        when(tender.getInvitationDate()).thenReturn(java.sql.Date.valueOf(date));

        AwardNotification an = awardNotification(tp, APPROVED);

        AwardNotificationItem ai = mock(AwardNotificationItem.class);
        when(ai.getParent()).thenReturn(an);
        when(ai.getAwardDate()).thenReturn(java.sql.Date.valueOf(date.plusDays(5)));
        when(an.getItems()).thenReturn(ImmutableList.of(ai));
        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(0));

        when(ai.getAwardDate()).thenReturn(java.sql.Date.valueOf(date.minusDays(5)));
        validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));
    }

    @Test
    public void testContractAwardAcceptanceDates() {
        LocalDate date = LocalDate.now();
        TenderProcess tp = mock(TenderProcess.class);
        AwardAcceptance aa = awardAcceptance(tp, APPROVED);
        AwardAcceptanceItem ai = mock(AwardAcceptanceItem.class);
        when(ai.getAcceptanceDate()).thenReturn(java.sql.Date.valueOf(date.plusDays(5)));
        when(aa.getAcceptedAcceptance()).thenReturn(ai);


        Contract c = contract(tp, APPROVED);
        when(c.getContractDate()).thenReturn(java.sql.Date.valueOf(date));

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));

        when(ai.getAcceptanceDate()).thenReturn(java.sql.Date.valueOf(date.minusDays(5)));
        validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(0));
    }

    @Test
    public void testAwardAcceptanceNotificationSuppliers() {
        TenderProcess tp = mock(TenderProcess.class);
        AwardAcceptance aa = awardAcceptance(tp, APPROVED);

        Supplier s1 = mock(Supplier.class);
        //when(s1.getLabel()).thenReturn("Supplier 1");
        when(aa.getAwardee()).thenReturn(ImmutableList.of(s1));

        AwardNotification an = awardNotification(tp, APPROVED);
        Supplier s2 = mock(Supplier.class);
        //when(s2.getLabel()).thenReturn("Supplier 2");
        when(an.getAwardee()).thenReturn(ImmutableList.of(s2));

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));

        when(an.getAwardee()).thenReturn(ImmutableList.of(s1));
        validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(0));
    }

    @Test
    public void testFullSupplierLink() {
        LocalDate date = LocalDate.now();
        TenderProcess tp = mock(TenderProcess.class);
        TenderQuotationEvaluation qe = quotationEvaluation(tp, APPROVED);

        Tender tender = tender(tp, APPROVED);
        when(tender.getInvitationDate()).thenReturn(java.sql.Date.valueOf(date));

        Supplier s1 = mock(Supplier.class);
        Bid b = mock(Bid.class);
        when(b.getSupplier()).thenReturn(s1);
        when(b.getSupplierResponsiveness()).thenReturn(DBConstants.SupplierResponsiveness.PASS);
        when(qe.getBids()).thenReturn(ImmutableList.of(b));

        ProfessionalOpinion po = professionalOpinion(tp, APPROVED);
        ProfessionalOpinionItem pi = mock(ProfessionalOpinionItem.class);
        when(pi.getAwardee()).thenReturn(s1);
        when(po.getItems()).thenReturn(ImmutableList.of(pi));

        AwardNotification an = awardNotification(tp, APPROVED);
        AwardNotificationItem ai = mock(AwardNotificationItem.class);
        when(ai.getAwardee()).thenReturn(s1);
        when(an.getItems()).thenReturn(ImmutableList.of(ai));
        when(an.getAwardee()).thenReturn(ImmutableList.of(s1));
        when(ai.getAwardDate()).thenReturn(java.sql.Date.valueOf(date.plusDays(3)));

        AwardAcceptance aa = awardAcceptance(tp, APPROVED);
        AwardAcceptanceItem aai = mock(AwardAcceptanceItem.class);
        when(aai.getAcceptanceDate()).thenReturn(java.sql.Date.valueOf(date.plusDays(5)));
        when(aai.getAwardee()).thenReturn(s1);
        when(aai.isAccepted()).thenReturn(true);
        when(aa.getItems()).thenReturn(ImmutableList.of(aai));
        when(aa.getAwardee()).thenReturn(ImmutableList.of(s1));
        when(aa.getAcceptedAcceptance()).thenReturn(aai);


        Contract c = contract(tp, APPROVED);
        when(c.getAwardee()).thenReturn(s1);
        when(c.getContractDate()).thenReturn(java.sql.Date.valueOf(date.plusDays(7)));

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(0));
    }


    @Test
    public void testDraftProjectWithNonDraftChildren() {

        TenderProcess tp = mock(TenderProcess.class);
        tender(tp, APPROVED);
        project(tp, DRAFT);
        awardAcceptance(tp, APPROVED);

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));
    }
}
