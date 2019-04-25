package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProfessionalOpinionPage;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-24
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/professionalOpinion")
public class EditProfessionalOpinionPage extends EditAbstractMakueniFormPage<ProfessionalOpinion> {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected SupplierService supplierService;

    public EditProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = professionalOpinionService;
        this.listPageClass = ListProfessionalOpinionPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();

        ComponentUtil.addDateField(editForm, "professionalOpinionDate").required();

        // TODO - this should be filtered based on list of suppliers in the Tender Quotation and Evaluation
        ComponentUtil.addSelect2ChoiceField(editForm, "awardee", supplierService).required();
        ComponentUtil.addDoubleField(editForm, "recommendedAwardAmount").required();

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected ProfessionalOpinion newInstance() {
        final ProfessionalOpinion professionalOpinion = super.newInstance();
        // professionalOpinion.setProcurementPlan(procurementPlan);  // here we need to set the ProcurementPlan
        return professionalOpinion;
    }
}
