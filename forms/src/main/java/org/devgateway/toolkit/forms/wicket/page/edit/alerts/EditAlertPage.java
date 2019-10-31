package org.devgateway.toolkit.forms.wicket.page.edit.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertPage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 30/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/alert")
public class EditAlertPage extends AbstractEditPage<Alert> {
    @SpringBean
    private AlertService alertService;

    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private ItemService itemService;

    public EditAlertPage(final PageParameters parameters) {
        super(parameters);

        jpaService = alertService;
        listPageClass = ListAlertPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> email = ComponentUtil.addTextField(editForm, "email");
        email.required()
                .getField().add(RfcCompliantEmailAddressValidator.getInstance());

        if (editForm.getModelObject().getPurchaseReq() != null) {
            final TenderProcess purchaseReq = editForm.getModelObject().getPurchaseReq();
            final Tender tender = purchaseReq.getTender().stream().findFirst().get();

            editForm.add(new GenericSleepFormComponent("purchaseReq", new Model(tender.getTenderTitle())));
        } else {
            editForm.add(new GenericSleepFormComponent("purchaseReq", new Model("No Tender Selected")));
        }

        ComponentUtil.addSelect2MultiChoiceField(editForm, "departments", departmentService);
        ComponentUtil.addSelect2MultiChoiceField(editForm, "items", itemService);

        ComponentUtil.addIntegerTextField(editForm, "failCount");
        editForm.add(new GenericSleepFormComponent("lastChecked"));
        editForm.add(new GenericSleepFormComponent("lastSendDate"));
        editForm.add(new GenericSleepFormComponent("lastErrorMessage"));

        ComponentUtil.addCheckToggle(editForm, "emailVerified");
        ComponentUtil.addCheckToggle(editForm, "alertable");
    }
}
