package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;

import java.util.List;

public class DepartmentOverviewItem extends Panel {
    private static final long serialVersionUID = -2887946738171526100L;
    private Project project;
    private Boolean expanded = false;

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;
    private List<PurchaseRequisition> purchaseReqisitions;

    public DepartmentOverviewItem(final String id, final Project project) {
        super(id);
        this.project = project;
        purchaseReqisitions = purchaseRequisitionService.findByProject(project);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addGroupHeader();
        addTenderButton();
        addTenderList();
    }

    private void addGroupHeader() {
        final TransparentWebMarkupContainer hideableContainer = new TransparentWebMarkupContainer("projectsWrapper");
        hideableContainer.setOutputMarkupId(true);
        hideableContainer.setOutputMarkupPlaceholderTag(true);
        add(hideableContainer);

        final AjaxLink<Void> header = new AjaxLink<Void>("header") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                if (expanded) {
                    expanded = false;
                    target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('hide')");
                } else {
                    expanded = true;
                    target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('show')");
                }

            }
        };

        header.add(new Label("projectTitle", new Model<String>(this.project.getProjectTitle())));
        header.add(new Label("fiscalYear",
                new PropertyModel<String>(this.project.getProcurementPlan().getFiscalYear(), "label")));
        add(header);
    }

    private void addTenderButton() {
        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_PROJECT_ID, project.getId());
        final BootstrapBookmarkablePageLink<Void> addTenderButton = new BootstrapBookmarkablePageLink<Void>("addTender",
                EditPurchaseRequisitionPage.class, pageParameters, Buttons.Type.Success);
        addTenderButton.setLabel(new StringResourceModel("addTender", DepartmentOverviewItem.this, null));
        add(addTenderButton);

    }

    private void addTenderList() {
        ListView<PurchaseRequisition> purchaseReqisitionList = new ListView<PurchaseRequisition>("tenderList",
                purchaseReqisitions) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PurchaseRequisition> item) {
                item.add(new TenderItem("tender", item.getModelObject()));
            }
        };
        purchaseReqisitionList.setOutputMarkupId(true);
        add(purchaseReqisitionList);
    }
}
