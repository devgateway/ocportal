package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewProjectsOverview extends AbstractListViewStatus<Project> {
    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    public ListViewProjectsOverview(final String id, final IModel<List<Project>> model) {
        super(id, model);
    }

    @Override
    protected void populateCompoundListItem(final ListItem<Project> item) {

    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<Project> item) {
        header.add(AttributeAppender.append("class", "project"));   // add specific class to project overview header

        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        final Project project = item.getModelObject();

        headerFragment.add(new DeptOverviewStatusLabel("projectStatus", project));
        headerFragment.add(new Label("projectTitle"));
        headerFragment.add(new Label("procurementPlan.fiscalYear"));

        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, project.getId());
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<>("editProject",
                EditProjectPage.class, pageParameters, Buttons.Type.Success);
        headerFragment.add(button);

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<Project> item) {
        hideableContainer.add(AttributeAppender.append("class", "tender")); // add specific class to project list
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);
        final Project project = item.getModelObject();


        final BootstrapAjaxLink<Void> addPurchaseRequisition = new BootstrapAjaxLink<Void>("addPurchaseRequisition",
                Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                SessionUtil.setSessionProject(project);
                setResponsePage(EditPurchaseRequisitionPage.class);
            }
        };
        addPurchaseRequisition.setLabel(
                new StringResourceModel("addPurchaseRequisition", ListViewProjectsOverview.this, null));
        containerFragment.add(addPurchaseRequisition);


        final List<PurchaseRequisition> purchaseRequisitions = purchaseRequisitionService.findByProject(project);
        final ListView<PurchaseRequisition> purchaseReqisitionList = new ListView<PurchaseRequisition>("tenderList",
                purchaseRequisitions) {
            @Override
            protected void populateItem(final ListItem<PurchaseRequisition> item) {
                item.add(new DeptOverviewPurchaseRequisitionPanel("tender", item.getModelObject()));
            }
        };
        purchaseReqisitionList.setOutputMarkupId(true);
        purchaseReqisitionList.setReuseItems(true);
        containerFragment.add(purchaseReqisitionList);


        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(final ListItem<Project> item) {
        return item.getModelObject().getId();
    }
}
