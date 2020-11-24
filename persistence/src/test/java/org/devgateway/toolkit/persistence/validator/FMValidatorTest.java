package org.devgateway.toolkit.persistence.validator;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.SUBMITTED;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.test.AbstractPersistenceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import javax.validation.Validator;
import java.util.HashSet;


/**
 * @author Octavian Ciubotaru
 */
public class FMValidatorTest extends AbstractPersistenceTest {

    @MockBean
    private DgFmService dgFmService;

    @Autowired
    private ApplicationContext context;

    @Before
    public void beforeTest() {
        given(dgFmService.hasFeature(any())).willReturn(true);
    }

    private Validator createValidator() {
        return context.getAutowireCapableBeanFactory().createBean(HibernateValidatorFactoryBean.class);
    }

    @Test
    public void testFieldIsRequiredWhenSubmitted() {
        given(dgFmService.isFeatureMandatory("tenderForm.tenderNumber")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
    }

    @Test
    public void testFieldIsOptionalWhenDraft() {
        given(dgFmService.isFeatureMandatory("tenderForm.tenderNumber")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setStatus(DRAFT);

        assertThat(validator.validate(tender), emptyIterable());
    }

    @Test
    public void testCommonFieldWithDifferentConstraints() {
        given(dgFmService.isFeatureMandatory("tenderForm.formDocs")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setStatus(SUBMITTED);

        ProcurementPlan procurementPlan = new ProcurementPlan();
        procurementPlan.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
        assertThat(validator.validate(procurementPlan), emptyIterable());
    }

    @Test
    public void testCascadeValidation() {
        given(dgFmService.isFeatureMandatory("tenderForm.tenderItems.description")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.getTenderItems().add(new TenderItem());
        tender.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
    }

    @Test
    public void testRequireEmptyString() {
        given(dgFmService.isFeatureMandatory("tenderForm.tenderNumber")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setTenderNumber("");
        tender.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
    }

    @Test
    public void testRequireNullCollection() {
        given(dgFmService.isFeatureMandatory("tenderForm.formDocs")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setFormDocs(null);
        tender.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
    }

    @Test
    public void testRequireEmptyCollection() {
        given(dgFmService.isFeatureMandatory("tenderForm.formDocs")).willReturn(true);

        Validator validator = createValidator();

        Tender tender = new Tender();
        tender.setFormDocs(new HashSet<>());
        tender.setStatus(SUBMITTED);

        assertThat(validator.validate(tender), hasSize(1));
    }

    @Test
    public void testDanglingObjectValidation() {
        given(dgFmService.isFeatureMandatory("tenderForm.tenderItems.description")).willReturn(true);

        Validator validator = createValidator();

        TenderItem tenderItem = new TenderItem();

        assertThat(validator.validate(tenderItem), hasSize(1));
    }

    @Test(expected = RuntimeException.class)
    public void testConflictingConstraintsForSharedObjects() {
        given(dgFmService.isFeatureMandatory("tenderForm.statusComments.comment")).willReturn(true);
        given(dgFmService.isFeatureMandatory("procurementPlanForm.statusComments.comment")).willReturn(false);

        createValidator();
    }
}
