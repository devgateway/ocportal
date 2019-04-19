/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSupplierPage;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
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

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addCode();
        TextAreaFieldBootstrapFormComponent<String> address = ComponentUtil.addTextAreaField(editForm, "address");        
        address.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);  
        
    }
}
