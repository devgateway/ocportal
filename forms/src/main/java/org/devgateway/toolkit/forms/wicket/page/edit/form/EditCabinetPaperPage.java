package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/cabinetPaper")
public class EditCabinetPaperPage extends AbstractEditPage<CabinetPaper> {

    @SpringBean
    protected CabinetPaperService cabinetPaperService;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    private final ProcurementPlan procurementPlan;

    public EditCabinetPaperPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = cabinetPaperService;
        this.listPageClass = DepartmentOverviewPage.class;
        this.procurementPlan = SessionUtil.getSessionPP();
        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && this.procurementPlan == null) {
            logger.warn("Something wrong happened since we are trying to add a new CabinetPaper Entity "
                    + "without having a ProcurementPlan!");
            setResponsePage(StatusOverviewPage.class);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()) == null) {
            setResponsePage(listPageClass);
        }

        final TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(editForm, "name");
        name.required();
        name.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        name.getField().add(uniqueName());

        final TextFieldBootstrapFormComponent<String> numberField = ComponentUtil.addTextField(editForm, "number");
        numberField.required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);

        numberField.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueCabinetPaperNumber"),
                cabinetPaperService::findOne,
                (o, v) -> (root, query, cb) -> cb.equal(cb.lower(root.get(CabinetPaper_.number)), v.toLowerCase()),
                editForm.getModel()
        ));


        final FileInputBootstrapFormComponent doc = new FileInputBootstrapFormComponent("formDocs");
        doc.maxFiles(1);
        doc.required();
        editForm.add(doc);
    }

    @Override
    protected CabinetPaper newInstance() {
        final CabinetPaper cabinetPaper = super.newInstance();
        cabinetPaper.setProcurementPlan(procurementPlan);

        return cabinetPaper;
    }

    private IValidator<String> uniqueName() {
        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new UniqueNameValidator(id.toLong(-1));
    }

    public class UniqueNameValidator implements IValidator<String> {
        private final Long id;

        public UniqueNameValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String name = validatable.getValue();
            final ProcurementPlan procurementPlan = editForm.getModelObject().getProcurementPlan();

            if (procurementPlan != null && name != null) {
                if (cabinetPaperService.countByProcurementPlanAndNameAndIdNot(procurementPlan, name, id) > 0) {
                    final ValidationError error = new ValidationError(getString("uniqueCabinetPaperName"));
                    validatable.error(error);
                }
            }
        }
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (isViewMode()) {
            send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());
        }

        saveButton.setVisibilityAllowed(!isViewMode());
        deleteButton.setVisibilityAllowed(!isViewMode());
        // no need to display the buttons on print view so we overwrite the above permissions
        if (ComponentUtil.isPrintMode()) {
            saveButton.setVisibilityAllowed(false);
            deleteButton.setVisibilityAllowed(false);
        }
    }

    private boolean isViewMode() {
        return SecurityConstants.Action.VIEW.equals(
                permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()));
    }
}
