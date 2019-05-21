package org.devgateway.toolkit.forms.wicket.page.overview.department;

import java.util.List;

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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class DepartmentOverviewItem extends Panel {
    private static final long serialVersionUID = -2887946738171526100L;

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    private final Project project;

    private final List<PurchaseRequisition> purchaseRequisitions;

    private Boolean expanded = false;

    public DepartmentOverviewItem(final String id, final Project project) {
        super(id);

        this.project = project;
        purchaseRequisitions = purchaseRequisitionService.findByProject(project);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addGroupHeader();
        addPurchaseRequisitionButton();
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

        header.add(new DeptOverviewStatusLabel("projectStatus", project));
        header.add(new Label("projectTitle", new Model<>(this.project.getProjectTitle())));
        header.add(new Label("fiscalYear",
                new PropertyModel<String>(this.project.getProcurementPlan().getFiscalYear(), "label")));
        add(header);
    }

   private void addPurchaseRequisitionButton() {        
        final BootstrapAjaxLink<Void> addPurchaseRequisition = new BootstrapAjaxLink<Void>("addPurchaseRequisition",
                Buttons.Type.Success) {       
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                SessionUtil.setSessionProject(project);
                setResponsePage(EditPurchaseRequisitionPage.class);                
            }            
        };
        
        addPurchaseRequisition.setLabel(new StringResourceModel("addPurchaseRequisition", 
                DepartmentOverviewItem.this, null));        
        add(addPurchaseRequisition);
   }

    private void addTenderList() {
        ListView<PurchaseRequisition> purchaseReqisitionList = new ListView<PurchaseRequisition>("tenderList",
                purchaseRequisitions) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<PurchaseRequisition> item) {
                item.add(new DeptOverviewPurchaseRequisitionPanel("tender",  item.getModelObject()));
            }
        };
        purchaseReqisitionList.setOutputMarkupId(true);
        add(purchaseReqisitionList);
    }
}
