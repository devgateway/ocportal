package org.devgateway.toolkit.forms.wicket.page.overview;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;

import java.util.List;


public class SideBar extends Panel {
    @SpringBean
    private DepartmentService departmentService;

    public SideBar(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        // TODO - here we should share more info than the departments, like logo, project count...
        final List<Department> departments = departmentService.findDeptsInCurrentProcurementPlan();
        add(new PropertyListView<Department>("departmentOverviewLink", departments) {
            @Override
            protected void populateItem(final ListItem<Department> item) {
                Link<Object> link = new Link<Object>("link") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        PageParameters parameters = new PageParameters();
                        parameters.add(WebConstants.PARAM_DEPARTMENT_ID, item.getModelObject().getId());
                        setResponsePage(DepartmentOverviewPage.class, parameters);
                    }
                };
                link.add(new Label("label", item.getModelObject().getLabel())
                        .setRenderBodyOnly(true));
                item.add(link);
            }
        });
    }

}
