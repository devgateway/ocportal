package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListContractDocumentPage;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocument_;
import org.devgateway.toolkit.persistence.service.category.ContractDocumentService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;


/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/contractdocument")
public class EditContractDocumentPage extends AbstractCategoryEditPage<ContractDocument> {

    @SpringBean
    private ContractDocumentService service;

    public EditContractDocumentPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListContractDocumentPage.class;
    }
    
    private void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueName"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                        ContractDocument_.label), v), editForm.getModel()
        ));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addUniqueNameValidator();
    }
}
