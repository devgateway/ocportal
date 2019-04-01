package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListItemPage;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.categories.Item_;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/item")
public class EditItemPage extends AbstractCategoryEditPage<Item> {
    @SpringBean
    private ItemService service;

    public EditItemPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListItemPage.class;
    }

    private void addCode() {
        final TextFieldBootstrapFormComponent<String> code = ComponentUtil.addTextField(editForm, "itemCode",
                false);
        code.required();
        
        code.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueItemCode"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Item_.itemCode), v), editForm.getModel()
        ));

    }
    
    private void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueName"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                Item_.label), v), editForm.getModel()
        ));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        addCode();
        addUniqueNameValidator();
    }

}
