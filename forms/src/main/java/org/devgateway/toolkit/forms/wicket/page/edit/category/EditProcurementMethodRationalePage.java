package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProcurementMethodRationalePage;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethodRationale;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodRationaleService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class EditProcurementMethodRationalePage extends AbstractCategoryEditPage<ProcurementMethodRationale> {

    @SpringBean
    private ProcurementMethodRationaleService departmentService;

    public EditProcurementMethodRationalePage(final PageParameters parameters) {
        super(parameters);
        jpaService = departmentService;
        listPageClass = ListProcurementMethodRationalePage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
