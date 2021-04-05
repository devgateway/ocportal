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
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowGroup;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowInfo;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-24
 */
public class ListViewStatusOverview extends AbstractListViewStatus<StatusOverviewRowGroup> {

    private final Boolean tenderProcessView;

    public ListViewStatusOverview(final String id, final IModel<List<StatusOverviewRowGroup>> model,
                                  Boolean tenderProcessView) {
        super(id, model);
        this.tenderProcessView = tenderProcessView;
    }

    @Override
    protected void populateCompoundListItem(final ListItem<StatusOverviewRowGroup> item) {
    }

    @Override
    protected void populateHeader(final String headerFragmentId,
                                  final AjaxLink<Void> header,
                                  final ListItem<StatusOverviewRowGroup> item) {
        final Fragment headerFragment = new Fragment(headerFragmentId, "headerFragment", this);

        headerFragment.add(new Label("procurementPlan.department"));
        headerFragment.add(new Label("procurementPlan.fiscalYear"));
        headerFragment.add(new Label("rowCount", item.getModelObject().getRows().size()));
        headerFragment.add(new Label("countLabel", new StringResourceModel(
                tenderProcessView ? "tenderProcesses" : "projects",
                ListViewStatusOverview.this)));

        header.add(headerFragment);
    }

    @Override
    protected void populateHideableContainer(final String containerFragmentId,
                                             final TransparentWebMarkupContainer hideableContainer,
                                             final ListItem<StatusOverviewRowGroup> item, boolean expanded) {
        final Fragment containerFragment = new Fragment(containerFragmentId, "containerFragment", this);

        final ProcurementPlan procurementPlan = item.getModelObject().getProcurementPlan();

        // add the project list
        containerFragment.add(new ListView<StatusOverviewRowInfo>("rows") {
            @Override
            protected void populateItem(final ListItem<StatusOverviewRowInfo> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<StatusOverviewRowInfo> compoundPropertyModel
                        = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model,
                // thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                final Link<Void> link = new Link<Void>("titleLink") {
                    @Override
                    public void onClick() {
                        sessionMetadataService.setSessionDepartment(procurementPlan.getDepartment());
                        if (tenderProcessView) {
                            sessionMetadataService.setSessionTenderProcessId(item.getModelObject().getId());
                        } else {
                            sessionMetadataService.setSessionProjectId(item.getModelObject().getId());
                        }
                        setResponsePage(DepartmentOverviewPage.class);
                    }
                };
                link.add(new Label("title"));
                item.add(link);

                link.add(addStatus("projectStatus", item.getModelObject().getProjectStatus(), ""));

                link.add(addStatus("tenderLabel", item.getModelObject().getTenderProcessStatus(),
                        getString("tenderProcess")));
                link.add(addStatus("tenderProcessStatus", item.getModelObject().getTenderProcessStatus(), ""));

                link.add(addStatus("awardLabel", item.getModelObject().getAwardProcessStatus(),
                        getString("awardProcess")));
                link.add(addStatus("awardProcessStatus", item.getModelObject().getAwardProcessStatus(), ""));

                link.add(addStatus("implementationLabel", item.getModelObject().getImplementationStatus(),
                        getString("implementationProcess")));

                link.add(addStatus("implementationProcessStatus", item.getModelObject().getImplementationStatus(), ""));
            }

            private Label addStatus(final String id, final String status, final String label) {
                return new Label(id, new Model<>(label)) {
                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);
                        if (status == null) {
                            setVisibilityAllowed(false);
                            return;
                        }

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

        hideableContainer.addOrReplace(containerFragment);
    }

    @Override
    protected Long getItemId(final ListItem<StatusOverviewRowGroup> item) {
        return item.getModelObject().getProcurementPlan().getId();
    }
}
