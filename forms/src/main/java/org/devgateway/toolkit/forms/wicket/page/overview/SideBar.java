package org.devgateway.toolkit.forms.wicket.page.overview;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;


public class SideBar extends Panel {
    private static final long serialVersionUID = 1L;
     
    @SpringBean
    private StatusOverviewService statusOverviewService;

    public SideBar(final String id) {
        super(id);
    }
    @Override
    protected void onInitialize() {
        super.onInitialize();        
        List<Department> departments = statusOverviewService.findDeptsInCurrentProcurementPlan();
        add(new PropertyListView<Department>("departmentOverviewLink", departments) {
            @Override
            protected void populateItem(final ListItem<Department> item) {                              
                Link<Object> link = new Link<Object>("link") {
                    private static final long   serialVersionUID    = 1L;

                    @Override
                    public void onClick() {
                        PageParameters parameters = new PageParameters();                        
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
