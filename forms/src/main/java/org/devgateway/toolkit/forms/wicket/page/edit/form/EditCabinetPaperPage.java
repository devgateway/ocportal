package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper_;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/cabinetPaper")
public class EditCabinetPaperPage extends AbstractEditPage<CabinetPaper> {
    
    @SpringBean
    protected CabinetPaperService cabinetPaperService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    public EditCabinetPaperPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = cabinetPaperService;
        this.listPageClass = ListCabinetPaperPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()) == null) {
            setResponsePage(listPageClass);
        }
        
        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();       
        ComponentUtil.addTextField(editForm, "name").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);

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
    protected void onBeforeRender() {
        super.onBeforeRender();

        if (isViewMode()) {
            send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());
        }

        saveButton.setVisibilityAllowed(!isViewMode());
        deleteButton.setVisibilityAllowed(!isViewMode());
        // no need to display the buttons on print view so we overwrite the above permissions
        if (ComponentUtil.isViewMode()) {
            saveButton.setVisibilityAllowed(false);
            deleteButton.setVisibilityAllowed(false);
        }
    }

    private boolean isViewMode() {
        return SecurityConstants.Action.VIEW.equals(
                permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()));
    }
}
