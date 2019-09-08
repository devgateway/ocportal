package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewProjectsOverview extends AbstractListViewStatus<Project> {
    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    private final Map<Project, List<PurchaseRequisition>> purchaseRequisitions;

    private final PurchaseRequisition sessionPurchaseRequisition;

    private final Boolean canAccessAddNewButtonInDeptOverview;

    public ListViewProjectsOverview(final String id, final IModel<List<Project>> model,
                                    final IModel<ProcurementPlan> procurementPlanModel) {
        super(id, model);

        // check if we need to expand a Project
        if (sessionMetadataService.getSessionProject() != null) {
            expandedContainerIds.add(sessionMetadataService.getSessionProject().getId());
        }

        sessionPurchaseRequisition = sessionMetadataService.getSessionPurchaseRequisition();

        purchaseRequisitions = purchaseRequisitionService.findByProjectProcurementPlan(procurementPlanModel.getObject())
                .parallelStream()
                .collect(Collectors.groupingBy(PurchaseRequisition::getProject,
                        Collectors.mapping(Function.identity(), Collectors.toList())));

        canAccessAddNewButtonInDeptOverview = ComponentUtil.canAccessAddNewButtonInDeptOverview(sessionMetadataService);
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
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<Void>("editProject",
                EditProjectPage.class, pageParameters, Buttons.Type.Success) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);

                if (!canAccessAddNewButtonInDeptOverview) {
                    Attributes.removeClass(tag, "btn-edit");
                    Attributes.addClass(tag, "btn-view");
                }
            }
        };
        button.add(new TooltipBehavior(Model.of("Edit/View Project")));
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
                sessionMetadataService.setSessionProject(project);
                setResponsePage(EditPurchaseRequisitionPage.class);
            }
        };
        addPurchaseRequisition.setLabel(
                new StringResourceModel("addPurchaseRequisition", ListViewProjectsOverview.this, null));
        containerFragment.add(addPurchaseRequisition);
        addPurchaseRequisition.setVisibilityAllowed(canAccessAddNewButtonInDeptOverview);

        // sort the purchase requisition list
        final List<PurchaseRequisition> purchaseReqs = purchaseRequisitions.get(project);
        if (purchaseReqs != null && !purchaseReqs.isEmpty()) {
            purchaseReqs.sort(Comparator.comparing(PurchaseRequisition::getId));
        }

        final ListViewPurchaseRequisitionOverview listViewPurchaseRequisitionOverview =
                new ListViewPurchaseRequisitionOverview("purchaseReqOverview",
                        new ListModel<>(purchaseReqs), sessionPurchaseRequisition);
        containerFragment.add(listViewPurchaseRequisitionOverview);

        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(final ListItem<Project> item) {
        return item.getModelObject().getId();
    }
}
