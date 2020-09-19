package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSubWardPage;
import org.devgateway.toolkit.persistence.dao.categories.SubWard;
import org.devgateway.toolkit.persistence.service.category.SubWardService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/subward")
public class EditSubWardPage extends AbstractLocationPointCategoryEditPage<SubWard> {
    @SpringBean
    private SubWardService subWardService;

    @SpringBean
    private WardService wardService;

    public EditSubWardPage(final PageParameters parameters) {
        super(parameters);
        jpaService = subWardService;
        listPageClass = ListSubWardPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        x.getField().setRequired(false);
        y.getField().setRequired(false);

        ComponentUtil.addSelect2ChoiceField(editForm, "ward", wardService).required();
    }
}
