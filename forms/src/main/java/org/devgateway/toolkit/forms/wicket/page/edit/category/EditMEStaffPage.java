/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListMEStaffPage;
import org.devgateway.toolkit.persistence.dao.categories.MEStaff;
import org.devgateway.toolkit.persistence.service.category.MEStaffService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostlenicu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class EditMEStaffPage extends AbstractCategoryEditPage<MEStaff> {

    @SpringBean
    private MEStaffService staffService;

    public EditMEStaffPage(final PageParameters parameters) {
        super(parameters);
        jpaService = staffService;
        listPageClass = ListMEStaffPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> phone = ComponentUtil.addTextField(editForm, "phone");
        phone.required();
    }
}
