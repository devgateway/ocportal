package org.devgateway.toolkit.forms.wicket.page.overview;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;
import org.devgateway.toolkit.persistence.dto.ProjectStatus;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;

public class DepartmentGroupItem extends Panel {
    private static final long serialVersionUID = 1L;

    private DepartmentOverviewData departmentOverviewData;
    private Boolean expanded = false;

    public DepartmentGroupItem(final String id, final DepartmentOverviewData departmentOverviewData) {
        super(id);
        this.departmentOverviewData = departmentOverviewData;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addGroupHeader();

        add(new ListView<ProjectStatus>("projectList", this.departmentOverviewData.getProjects()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<ProjectStatus> item) {
                // item.add(new Label("title", new PropertyModel<String>(item.getModelObject(),
                // "projectTitle")));

                Link<Object> link = new Link<Object>("title") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.add("id", item.getModelObject().getId());
                        setResponsePage(ProjectOverviewPage.class, parameters);
                    }
                };
                link.add(new Label("label", new PropertyModel<String>(item.getModelObject(), "projectTitle")));
                item.add(link);

                addStatusLabel(item, "projectStatus", item.getModelObject().getProjectStatus().toLowerCase(), null);
                addStatusLabel(item, "tenderStatus", item.getModelObject().getTenderStatus().toLowerCase(), null);
                addStatusLabel(item, "tenderProcess", getCssClass(item.getModelObject().getTenderStatus()),
                        getString("tenderProcess"));
                addStatusLabel(item, "awardStatus", item.getModelObject().getAwardStatus().toLowerCase(), null);
                addStatusLabel(item, "awardProcess", getCssClass(item.getModelObject().getAwardStatus()),
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

    private void addStatusLabel(final ListItem<ProjectStatus> item, final String id, final String cssClass,
            final String message) {
        Label label;
        if (message != null) {
            label = new Label(id, Model.of(message));
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
                new PropertyModel<String>(departmentOverviewData.getProcurementPlan().getDepartment(), "label")));
        header.add(new Label("year",
                new PropertyModel<String>(departmentOverviewData.getProcurementPlan().getFiscalYear(), "label")));
        header.add(new Label("projectCount", new PropertyModel<String>(departmentOverviewData.getProjects(), "size")));

    }
}
