package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.util.JQueryUtil;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.EditViewResourceModel;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionGroupPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

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
    private TenderProcessService tenderProcessService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    private final Map<Project, List<TenderProcess>> tenderProcesses;

    private final Project sessionProject;

    private final TenderProcess sessionTenderProcess;

    public ListViewProjectsOverview(final String id, final IModel<List<Project>> model,
                                    final IModel<ProcurementPlan> procurementPlanModel) {
        super(id, model);

        // check if we need to expand a Project
        sessionProject = sessionMetadataService.getSessionProject();
        if (sessionProject != null) {
            expandedContainerIds.add(sessionProject.getId());
        }

        sessionTenderProcess = sessionMetadataService.getSessionTenderProcess();

        tenderProcesses = tenderProcessService.findByProjectProcurementPlan(procurementPlanModel.getObject())
                .parallelStream()
                .filter(tp -> tp.getProject() != null)
                .collect(Collectors.groupingBy(TenderProcess::getProject,
                        Collectors.mapping(Function.identity(), Collectors.toList())));
    }

    public boolean canAccessAddNewButtons(Class<? extends AbstractEditPage<?>> clazz) {
        return ComponentUtil.canAccessAddNewButtons(clazz, permissionEntityRenderableService,
                sessionMetadataService);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // scroll to the last edited item (see: OCMAKU-135)
        if (this.getModelObject() != null && sessionProject != null && sessionTenderProcess == null) {
            if (this.getModelObject().contains(sessionProject)) {
                response.render(OnDomReadyHeaderItem.forScript(
                        JQueryUtil.animateScrollTop("#" + "project-header-" + sessionProject.getId(), 100, 500)));
            }
        }
    }

    @Override
    protected void populateCompoundListItem(final ListItem<Project> item) {

    }

    @Override
    @Transactional(readOnly = true)
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<Project> item) {
        header.add(AttributeAppender.append("class", "project"));   // add specific class to project overview header

        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);
        headerFragment.setMarkupId("project-header-" + item.getModelObject().getId());

        //final Project project = projectService.findById(item.getModelObject().getId()).get();

        headerFragment.add(new DeptOverviewStatusLabel("projectStatus", item.getModelObject().getStatus()));
        headerFragment.add(new Label("title"));
        headerFragment.add(new Label("procurementPlan.fiscalYear"));

        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, item.getModelObject().getId());
        final BootstrapAjaxLink<Void> button = new BootstrapAjaxLink<Void>("editProject",
                Buttons.Type.Success) {
            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);

                if (!canAccessAddNewButtons(EditProjectPage.class)) {
                    Attributes.removeClass(tag, "btn-edit");
                    Attributes.addClass(tag, "btn-view");
                }
            }

            @Override
            public void onClick(final AjaxRequestTarget target) {
                sessionMetadataService.setSessionProjectId(item.getModelObject().getId());

                final PageParameters pageParameters = new PageParameters();
                pageParameters.set(WebConstants.PARAM_ID, item.getModelObject().getId());
                setResponsePage(EditProjectPage.class, pageParameters);
            }
        };
        button.add(new TooltipBehavior(
                EditViewResourceModel.of(canAccessAddNewButtons(EditProjectPage.class), "project", this)));
        headerFragment.add(button);

        header.add(headerFragment);
    }

    @Override
    @Transactional(readOnly = true)
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<Project> item, boolean expanded) {

        hideableContainer.add(AttributeAppender.append("class", "tender")); // add specific class to project list
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);
        final Project project = projectService.findById(item.getModelObject().getId()).get();

        final BootstrapAjaxLink<Void> addTenderProcess = new BootstrapAjaxLink<Void>("addTenderProcess",
                Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                TenderProcess tenderProcess = tenderProcessService.newInstance();
                tenderProcess.setProject(project);
                tenderProcess.setProcurementPlan(sessionMetadataService.getSessionPP());
                TenderProcess savedTenderProcess = tenderProcessService.save(tenderProcess);
                sessionMetadataService.setSessionProject(project);
                sessionMetadataService.setSessionTenderProcess(savedTenderProcess);
                setResponsePage(DepartmentOverviewPage.class);
            }
        };
        addTenderProcess.setLabel(
                new StringResourceModel("addTenderProcess", ListViewProjectsOverview.this, null));
        containerFragment.add(addTenderProcess);
        addTenderProcess.setVisibilityAllowed(canAccessAddNewButtons(EditProjectPage.class));

        // sort the purchase requisition list
        final List<TenderProcess> purchaseReqs = tenderProcesses.get(project);
        if (purchaseReqs != null && !purchaseReqs.isEmpty()) {
            purchaseReqs.sort(Comparator.comparing(TenderProcess::getId));
        }

        final ListViewTenderProcessOverview listViewTenderProcessOverview =
                new ListViewTenderProcessOverview("purchaseReqOverview", false,
                        new ListModel<>(purchaseReqs), sessionTenderProcess);
        containerFragment.add(listViewTenderProcessOverview);

        hideableContainer.addOrReplace(containerFragment);
    }

    @Override
    protected Long getItemId(final ListItem<Project> item) {
        return item.getModelObject().getId();
    }
}
