package org.devgateway.toolkit.forms.wicket.page.overview.status;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
import org.devgateway.toolkit.persistence.dto.StatusOverviewProjectStatus;

import java.util.List;
import java.util.stream.Collectors;

public class StatusOverviewItem extends Panel {
    private static final long serialVersionUID = 1L;

    private StatusOverviewData statusOverviewData;

    private Boolean expanded = false;

    private String searchTerm;

    private List<StatusOverviewProjectStatus> filteredProjects;

    public StatusOverviewItem(final String id, final StatusOverviewData statusOverviewData,
                              final String searchTerm) {
        super(id);
        this.statusOverviewData = statusOverviewData;
        this.searchTerm = searchTerm;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        filteredProjects = this.statusOverviewData.getProjects();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            filteredProjects = this.statusOverviewData.getProjects().stream().filter(p -> {
                if (p.getProjectTitle() != null) {
                    return p.getProjectTitle().toLowerCase().contains(searchTerm.toLowerCase());
                }

                return false;
            }).collect(Collectors.toList());
        }

        addGroupHeader();

        add(new ListView<StatusOverviewProjectStatus>("projectList", filteredProjects) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<StatusOverviewProjectStatus> item) {
                Link<Object> link = new Link<Object>("title") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        // TODO
                        // PageParameters parameters = new PageParameters();
                        // parameters.add("id", item.getModelObject().getId());
                        // setResponsePage(DepartmentOverviewPage.class, parameters);
                    }
                };
                link.add(new Label("label", new PropertyModel<String>(item.getModelObject(), "projectTitle")));
                item.add(link);

                addStatusLabel(item, "projectStatus", item.getModelObject().getProjectStatus().toLowerCase(), null);
                addStatusLabel(item, "tenderProcessStatus",
                        item.getModelObject().getTenderProcessStatus().toLowerCase(), null);
                addStatusLabel(item, "tenderProcess", getCssClass(item.getModelObject().getTenderProcessStatus()),
                        getString("tenderProcess"));
                addStatusLabel(item, "awardProcessStatus",
                        item.getModelObject().getAwardProcessStatus().toLowerCase(), null);
                addStatusLabel(item, "awardProcess", getCssClass(item.getModelObject().getAwardProcessStatus()),
                        getString("awardProcess"));
            }
        });

    }

    private String getCssClass(final String status) {
        String cssClass;
        if (DBConstants.Status.NOT_STARTED.equals(status)) {
            cssClass = "inactive";
        } else {
            cssClass = status.toLowerCase();
        }

        return cssClass;
    }

    private void addStatusLabel(final ListItem<StatusOverviewProjectStatus> item, final String id, final String cssClass,
                                final String message) {
        Label label;
        if (message != null) {
            label = new Label(id, message);
        } else {
            label = new Label(id);
        }

        label.add(AttributeAppender.append("class", cssClass));
        item.add(label);
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

        add(header);

        header.add(new Label("departmentName",
                new PropertyModel<String>(statusOverviewData.getProcurementPlan().getDepartment(), "label")));
        header.add(new Label("year",
                new PropertyModel<String>(statusOverviewData.getProcurementPlan().getFiscalYear(), "label")));
        header.add(new Label("projectCount", new PropertyModel<String>(filteredProjects, "size")));
    }
}
