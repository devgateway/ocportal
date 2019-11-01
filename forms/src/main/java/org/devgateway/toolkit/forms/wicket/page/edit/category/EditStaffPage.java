/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListStaffPage;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.StaffService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/staff")
public class EditStaffPage extends AbstractCategoryEditPage<Staff> {
    @SpringBean
    private StaffService staffService;

    @SpringBean
    private DepartmentService departmentService;
    
    public EditStaffPage(final PageParameters parameters) {
        super(parameters);
        jpaService = staffService;
        listPageClass = ListStaffPage.class;
    }
  
    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> title = ComponentUtil.addTextField(editForm, "title");
        title.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);

        ComponentUtil.addSelect2ChoiceField(editForm, "department", departmentService);
    }
}
