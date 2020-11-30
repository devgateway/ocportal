package org.devgateway.toolkit.persistence.validator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.test.AbstractPersistenceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;

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
    public void testAwardAcceptanceNotificationSuppliers() throws Exception {

        TenderProcess tp = mock(TenderProcess.class);
        AwardAcceptance aa = awardAcceptance(tp, APPROVED);

        Supplier s1 = mock(Supplier.class);
        when(aa.getAwardee()).thenReturn(ImmutableList.of(s1));


        AwardNotification an = awardNotification(tp, APPROVED);
        Supplier s2 = mock(Supplier.class);
        when(an.getAwardee()).thenReturn(ImmutableList.of(s2));

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));

        validate = tenderProcessService.validate(tp);
        when(an.getAwardee()).thenReturn(ImmutableList.of(s1));
        assertThat(validate.getAllErrors(), hasSize(0));
    }

    @Test
    public void testDraftProjectWithNonDraftChildren() throws Exception {

        TenderProcess tp = mock(TenderProcess.class);
        tender(tp, APPROVED);
        project(tp, DRAFT);
        awardAcceptance(tp, APPROVED);

        BindingResult validate = tenderProcessService.validate(tp);
        assertThat(validate.getAllErrors(), hasSize(1));
    }
}
