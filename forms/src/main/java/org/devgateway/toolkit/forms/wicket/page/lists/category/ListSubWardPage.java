package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditSubWardPage;
import org.devgateway.toolkit.persistence.dao.categories.SubWard;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.service.category.SubWardService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.category.SubWardFilterState;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/subwards")
public class ListSubWardPage extends AbstractListCategoryPage<SubWard> {
    @SpringBean
    private SubWardService subWardService;

    @SpringBean
    private WardService wardService;

    public ListSubWardPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = subWardService;
        this.editPageClass = EditSubWardPage.class;
    }

    @Override
    protected void onInitialize() {
        final List<Ward> wards = wardService.findAll();
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new StringResourceModel("ward", this),
                "ward", "ward", new ListModel<>(wards), dataTable));

        super.onInitialize();
    }

    @Override
    public JpaFilterState<SubWard> newFilterState() {
        return new SubWardFilterState();
    }
}
