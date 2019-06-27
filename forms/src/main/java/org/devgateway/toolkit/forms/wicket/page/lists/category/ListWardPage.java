package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditWardPage;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.category.WardFilterState;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author idobre
 * @since 2019-06-26
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/wards")
public class ListWardPage extends AbstractListCategoryPage<Ward> {
    @SpringBean
    private WardService wardService;

    @SpringBean
    private SubcountyService subcountyService;

    public ListWardPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = wardService;
        this.editPageClass = EditWardPage.class;
    }

    @Override
    protected void onInitialize() {
        final List<Subcounty> subcounties = subcountyService.findAll();
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Sub-County"),
                "subcounty", "subcounty", new ListModel(subcounties), dataTable));

        super.onInitialize();
    }

    @Override
    public JpaFilterState<Ward> newFilterState() {
        return new WardFilterState();
    }
}
