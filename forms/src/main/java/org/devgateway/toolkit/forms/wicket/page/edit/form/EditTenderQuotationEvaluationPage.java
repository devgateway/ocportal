package org.devgateway.toolkit.forms.wicket.page.edit.form;


import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.BidPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderQuotationEvaluationPage;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvalutionService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tenderQuotationEvaluation")
public class EditTenderQuotationEvaluationPage extends EditAbstractMakueniFormPage<TenderQuotationEvaluation> {

    @SpringBean
    protected TenderQuotationEvalutionService tenderQuotationEvalutionService; 
    
    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected TenderService tenderService;
    
    private Tender tender = null;
    
    /**
     * @param parameters
     */
    public EditTenderQuotationEvaluationPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderQuotationEvalutionService;
        this.listPageClass = ListTenderQuotationEvaluationPage.class;
        
        StringValue tenderId = parameters.get(WebConstants.PARAM_TENDER_ID);
        if (!tenderId.isNull()) {
            tender = tenderService.findById(tenderId.toLong()).orElse(null);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();        
        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "tender", tenderService).required();
        
        ComponentUtil.addDateField(editForm, "closingDate").required();
        ComponentUtil.addIntegerTextField(editForm, "numberOfBids").required();
        editForm.add(new BidPanel("bids"));
        
        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }  
    
    @Override
    protected TenderQuotationEvaluation newInstance() {
        final TenderQuotationEvaluation tenderQuotationEvaluation = super.newInstance();
        if (tender != null) {
            tenderQuotationEvaluation.setProcurementPlan(tender.getProcurementPlan());   
            tenderQuotationEvaluation.setTender(tender);
        }
        
        return tenderQuotationEvaluation;
    }
}
