/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSupplierPage;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.Supplier_;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/supplier")
public class EditSupplierPage extends AbstractCategoryEditPage<Supplier> {
    @SpringBean
    private SupplierService supplierService;

    public EditSupplierPage(final PageParameters parameters) {
        super(parameters);
        jpaService = supplierService;
        listPageClass = ListSupplierPage.class;
    }

    private void addCode() {
        final TextFieldBootstrapFormComponent<String> code = ComponentUtil.addTextField(editForm, "code");
        code.required();
        
        code.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueCode"),
                supplierService::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Supplier_.code), v), editForm.getModel()
        ));

    }
    
    private void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueName"),
                supplierService::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Supplier_.label), v), editForm.getModel()
        ));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        addCode();
        addUniqueNameValidator();
        ComponentUtil.addTextAreaField(editForm, "address");
    }

}
