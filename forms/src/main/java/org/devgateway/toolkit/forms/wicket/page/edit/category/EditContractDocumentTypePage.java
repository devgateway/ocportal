package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListContractDocumentTypePage;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.devgateway.toolkit.persistence.service.category.ContractDocumentTypeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;


/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/ContractDocumentType")
public class EditContractDocumentTypePage extends AbstractCategoryEditPage<ContractDocumentType> {

    @SpringBean
    private ContractDocumentTypeService service;

    public EditContractDocumentTypePage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListContractDocumentTypePage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
