package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.TargetGroupFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditTargetGroupPage;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-03-11
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/targetgroups")
public class ListTargetGroupPage extends AbstractListCategoryPage<TargetGroup> {
    @SpringBean
    private TargetGroupService targetGroupService;

    public ListTargetGroupPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = targetGroupService;
        this.editPageClass = EditTargetGroupPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public JpaFilterState<TargetGroup> newFilterState() {
        return new TargetGroupFilterState();
    }
}
