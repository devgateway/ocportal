package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ItemDetailPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.TenderItemPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderPage;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.ProcuringEntityService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tender")
public class EditTenderPage extends EditAbstractMakueniFormPage<Tender> {

    @SpringBean
    protected TenderService tenderService;
    
    @SpringBean
    protected ProcurementPlanService procurementPlanService;
    
    @SpringBean
    protected ProcurementMethodService procurementMethodService;
    
    @SpringBean
    protected ProcuringEntityService procuringEntityService;
    
    /**
     * @param parameters
     */
    public EditTenderPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderService;
        this.listPageClass = ListTenderPage.class;
    }

    @Override
    protected void onInitialize() {
       super.onInitialize();
       ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();
       ComponentUtil.addTextField(editForm, "tenderNumber").required();
       ComponentUtil.addTextField(editForm, "tenderTitle").required();
       ComponentUtil.addDateField(editForm, "invitationDate");
       ComponentUtil.addDateField(editForm, "closingDate").required();
       ComponentUtil.addSelect2ChoiceField(editForm, "procurementMethod", procurementMethodService).required();
       ComponentUtil.addTextAreaField(editForm, "objective");
       ComponentUtil.addSelect2ChoiceField(editForm, "issuedBy", procuringEntityService);
       ComponentUtil.addDoubleField(editForm, "tenderValue");       
       editForm.add(new TenderItemPanel("tenderItems"));       
       ComponentUtil.addTextField(editForm, "tenderLink");
       final FileInputBootstrapFormComponent tenderDocs =
               new FileInputBootstrapFormComponent("tenderDocs");       
       tenderDocs.required();
       editForm.add(tenderDocs);  
       
    }
}
