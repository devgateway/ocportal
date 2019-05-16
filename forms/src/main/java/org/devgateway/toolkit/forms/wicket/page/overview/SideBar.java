package org.devgateway.toolkit.forms.wicket.page.overview;

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
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;

import java.util.List;


public class SideBar extends Panel {
    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    public SideBar(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Image logo = new Image("logo", new PackageResourceReference(BaseStyles.class, "assets/img/logo.png"));
        add(logo);

        // TODO - here we should share more info than the departments, like logo, project count...
        final FiscalYear lastFiscalYear = fiscalYearService.getLastFiscalYear();
        final List<Department> departments = departmentService.findActiveDepartmentsInFiscalYear(lastFiscalYear);
        add(new PropertyListView<Department>("departmentOverviewLink", departments) {
            @Override
            protected void populateItem(final ListItem<Department> item) {
                Link<Object> link = new Link<Object>("link") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        SessionUtil.setSessionDepartment(item.getModelObject());
                        setResponsePage(DepartmentOverviewPage.class);
                    }
                };
                link.add(new Label("label", item.getModelObject().getLabel())
                        .setRenderBodyOnly(true));
                item.add(link);
            }
        });
    }

}
