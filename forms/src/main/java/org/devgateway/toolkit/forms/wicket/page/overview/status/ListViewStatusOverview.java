package org.devgateway.toolkit.forms.wicket.page.overview.status;

import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
import org.devgateway.toolkit.persistence.dto.StatusOverviewProjectStatus;
import org.devgateway.toolkit.persistence.service.form.ProjectService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewStatusOverview extends AbstractListViewStatus<StatusOverviewData> {
    @SpringBean
    private ProjectService projectService;

    public ListViewStatusOverview(final String id, final IModel<List<StatusOverviewData>> model) {
        super(id, model);
    }

    @Override
    protected void populateCompoundListItem(final ListItem<StatusOverviewData> item) {
    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<StatusOverviewData> item) {
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);

        headerFragment.add(new Label("procurementPlan.department"));
        headerFragment.add(new Label("procurementPlan.fiscalYear"));
        headerFragment.add(new Label("projectCount", item.getModelObject().getProjects().size()));

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<StatusOverviewData> item) {
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);

        final ProcurementPlan procurementPlan = item.getModelObject().getProcurementPlan();

        // add the project list
        containerFragment.add(new ListView<StatusOverviewProjectStatus>("projects") {
            @Override
            protected void populateItem(final ListItem<StatusOverviewProjectStatus> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<StatusOverviewProjectStatus> compoundPropertyModel
                        = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model,
                // thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                final Link<Void> link = new Link<Void>("title") {
                    @Override
                    public void onClick() {
                        SessionUtil.setSessionDepartment(procurementPlan.getDepartment());
                        SessionUtil.setSessionProject(
                                projectService.findByIdCached(item.getModelObject().getId()).get());
                        setResponsePage(DepartmentOverviewPage.class);
                    }
                };
                link.add(new Label("projectTitle"));
                item.add(link);

                item.add(addStatus("projectStatus", item.getModelObject().getProjectStatus(), ""));

                item.add(addStatus("tenderLabel", item.getModelObject().getTenderProcessStatus(),
                        getString("tenderProcess")));
                item.add(addStatus("tenderProcessStatus", item.getModelObject().getTenderProcessStatus(), ""));

                item.add(addStatus("awardLabel", item.getModelObject().getAwardProcessStatus(),
                        getString("awardProcess")));
                item.add(addStatus("awardProcessStatus", item.getModelObject().getAwardProcessStatus(), ""));
            }

            private Label addStatus(final String id, final String status, final String label) {
                return new Label(id, new Model<>(label)) {
                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);

                        final String cssClass;
                        if (DBConstants.Status.NOT_STARTED.equals(status)) {
                            cssClass = "inactive";
                        } else {
                            cssClass = status.toLowerCase();
                        }

                        Attributes.addClass(tag, cssClass);
                    }
                };
            }
        });

        hideableContainer.add(containerFragment);
    }

    @Override
    protected Long getItemId(final ListItem<StatusOverviewData> item) {
        return item.getModelObject().getProcurementPlan().getId();
    }
}
