package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.TenderQuotationEvaluationFilterState;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tenderQuotationEvaluationList")
public class ListTenderQuotationEvaluationPage extends ListAbstractPurchaseReqMakueniEntity<TenderQuotationEvaluation> {
    
    @SpringBean
    protected TenderQuotationEvaluationService tenderQuotationEvaluationService;

    public ListTenderQuotationEvaluationPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderQuotationEvaluationService;
        this.editPageClass = EditTenderQuotationEvaluationPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();


        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("title", ListTenderQuotationEvaluationPage.this)).getString()),
                "purchaseRequisition.tender.tenderTitle",
                "purchaseRequisition.tender.tenderTitle"));



        addFileDownloadColumn();
    }

    @Override
    public JpaFilterState<TenderQuotationEvaluation> newFilterState() {
        return new TenderQuotationEvaluationFilterState();
    }
}