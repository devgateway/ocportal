package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.security.SecurityConstants;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.lists.ListDepartmentPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/department")
public class EditDepartmentPage extends AbstractCategoryEditPage<Department> {

    @SpringBean
    private DepartmentService service;

    public EditDepartmentPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListDepartmentPage.class;
    }


    public void addCode() {
        TextFieldBootstrapFormComponent<Integer> code = new TextFieldBootstrapFormComponent<>("code");
        code.integer().required();
        editForm.add(code);

        code.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueDepartmentCode"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Department_.code), v), editForm.getModel()
        ));
    }

    public void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueName"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Department_.label), v), editForm.getModel()
        ));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addCode();
        addUniqueNameValidator();
    }
}
