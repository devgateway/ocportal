package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListDepartmentPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/department")
public class EditDepartmentPage extends AbstractCategoryEditPage<Department> {

    @SpringBean
    private DepartmentService departmentService;

    public EditDepartmentPage(final PageParameters parameters) {
        super(parameters);
        jpaService = departmentService;
        listPageClass = ListDepartmentPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addCode();
    }
}
