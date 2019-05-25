/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListItemPage;
import org.devgateway.toolkit.persistence.dao.categories.Item;
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
    private ItemService itemService;

    public EditItemPage(final PageParameters parameters) {
        super(parameters);
        jpaService = itemService;
        listPageClass = ListItemPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addCode();
    }
}
