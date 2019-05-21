package org.devgateway.toolkit.forms.wicket.page.overview;

import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;

import java.util.List;


public class SideBar extends Panel {
    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private ProjectService projectService;

    private final Department department;

    private Label projectCount;

    public SideBar(final String id) {
        super(id);

        this.department = SessionUtil.getSessionDepartment();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Image logo = new Image("logo", new PackageResourceReference(BaseStyles.class, "assets/img/logo.png"));
        add(logo);

        projectCount = new Label("projectCount", calculateProjectCount());
        projectCount.setOutputMarkupId(true);
        add(projectCount);

        final Link<Void> title = new Link<Void>("title") {
            @Override
            public void onClick() {
                // clear all session data before going to the dashboard
                SessionUtil.clearSessionData();
                setResponsePage(StatusOverviewPage.class);
            }

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);

                if (department != null) {
                    Attributes.removeClass(tag, "selected");
                }
            }
        };
        add(title);

        final List<Department> departments = departmentService.findAll();
        add(new PropertyListView<Department>("departmentOverviewLink", departments) {
            @Override
            protected void populateItem(final ListItem<Department> item) {
                final Link<Void> link = new Link<Void>("link") {
                    @Override
                    public void onClick() {
                        SessionUtil.setSessionDepartment(item.getModelObject());
                        setResponsePage(DepartmentOverviewPage.class);
                    }

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);

                        if (item.getModelObject().equals(department)) {
                            Attributes.addClass(tag, "selected");
                        }
                    }
                };
                link.add(new Label("label", item.getModelObject().getLabel())
                        .setRenderBodyOnly(true));
                item.add(link);
            }
        });
    }

    private Long calculateProjectCount() {
        final FiscalYear fiscalYear = SessionUtil.getSessionFiscalYear();
        if (department == null) {
            return projectService.countByFiscalYear(fiscalYear);
        }

        return projectService.countByDepartmentAndFiscalYear(department, fiscalYear);
    }

    public Label getProjectCount() {
        return projectCount;
    }
}
