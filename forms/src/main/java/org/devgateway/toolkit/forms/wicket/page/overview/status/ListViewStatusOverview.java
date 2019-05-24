package org.devgateway.toolkit.forms.wicket.page.overview.status;

import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.CompoundSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
import org.devgateway.toolkit.persistence.dto.StatusOverviewProjectStatus;
import org.devgateway.toolkit.persistence.service.form.ProjectService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-05-17
 */
public class ListViewStatusOverview extends CompoundSectionPanel<List<StatusOverviewData>> {
    private WebMarkupContainer listWrapper;

    private ListView<StatusOverviewData> listView;

    @SpringBean
    private ProjectService projectService;

    private Set<Long> expandedContainerIds = new HashSet<>();

    public ListViewStatusOverview(final String id, final IModel<List<StatusOverviewData>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        listView = new ListView<StatusOverviewData>("list", getModel()) {
            @Override
            protected void populateItem(final ListItem<StatusOverviewData> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<StatusOverviewData> compoundPropertyModel
                        = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // add header
                final TransparentWebMarkupContainer hideableContainer =
                        new TransparentWebMarkupContainer("hideableContainer") {
                            @Override
                            protected void onComponentTag(final ComponentTag tag) {
                                super.onComponentTag(tag);

                                if (isExpanded(item)) {
                                    Attributes.addClass(tag, "in");
                                } else {
                                    Attributes.removeClass(tag, "in");
                                }
                            }
                        };
                hideableContainer.setOutputMarkupId(true);
                hideableContainer.setOutputMarkupPlaceholderTag(true);
                item.add(hideableContainer);

                final AjaxLink<Void> header = new AjaxLink<Void>("header") {
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        if (isExpanded(item)) {
                            target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('hide')");
                            expandedContainerIds.remove(getItemId(item));
                        } else {
                            target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('show')");
                            expandedContainerIds.add(getItemId(item));
                        }
                        target.add(this);
                    }

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);

                        if (isExpanded(item)) {
                            Attributes.removeClass(tag, "collapsed");
                        } else {
                            Attributes.addClass(tag, "collapsed");
                        }
                    }
                };
                item.add(header);

                header.add(new Label("procurementPlan.department"));
                header.add(new Label("procurementPlan.fiscalYear"));
                header.add(new Label("projectCount", item.getModelObject().getProjects().size()));

                final ProcurementPlan procurementPlan = item.getModelObject().getProcurementPlan();

                // add the items in the listItem
                item.add(new ListView<StatusOverviewProjectStatus>("projects") {
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
            }
        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);
    }

    protected Long getItemId(final ListItem<StatusOverviewData> item) {
        return item.getModelObject().getProcurementPlan().getId();
    }

    private boolean isExpanded(final ListItem<StatusOverviewData> item) {
        return expandedContainerIds.contains(getItemId(item));
    }
}
