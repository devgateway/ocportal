/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.category.ContractDocumentTypeFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditContractDocumentTypePage;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.devgateway.toolkit.persistence.service.category.ContractDocumentTypeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;


/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/ContractDocumentTypes")
public class ListContractDocumentTypePage extends AbstractListCategoryPage<ContractDocumentType> {
    @SpringBean
    private ContractDocumentTypeService service;

    public ListContractDocumentTypePage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = service;
        this.editPageClass = EditContractDocumentTypePage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public JpaFilterState<ContractDocumentType> newFilterState() {
        return new ContractDocumentTypeFilterState();
    }
}
