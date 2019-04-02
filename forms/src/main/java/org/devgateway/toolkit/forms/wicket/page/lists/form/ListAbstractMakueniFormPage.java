package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListStatusEntityPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniForm;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class ListAbstractMakueniFormPage<T extends AbstractMakueniForm>
        extends AbstractListStatusEntityPage<T> {
    @SpringBean
    private DepartmentService departmentService;

    public ListAbstractMakueniFormPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        columns.add(new TextFilteredBootstrapPropertyColumn<>(new Model<>("Status search"), "status", "status"));

        super.onInitialize();

        final List<Department> departments = departmentService.findAll();
        columns.add(3, new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "procurementPlan.department", "procurementPlan.department", new ListModel(departments), dataTable));

    }
}
