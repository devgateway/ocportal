/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.category.ContractDocumentFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditContractDocumentPage;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.devgateway.toolkit.persistence.service.category.ContractDocumentService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;


/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/contractdocuments")
public class ListContractDocumentPage extends AbstractListCategoryPage<ContractDocument> {
    @SpringBean
    private ContractDocumentService service;

    public ListContractDocumentPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = service;
        this.editPageClass = EditContractDocumentPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public JpaFilterState<ContractDocument> newFilterState() {
        return new ContractDocumentFilterState();
    }
}
