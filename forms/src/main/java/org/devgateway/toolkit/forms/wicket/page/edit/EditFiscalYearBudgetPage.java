package org.devgateway.toolkit.forms.wicket.page.edit;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.FiscalYearBudgetService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditFiscalYearBudgetPage extends AbstractEditPage<FiscalYearBudget> implements ProcurementRoleAssignable {
    @SpringBean
    private FiscalYearService fiscalYearService;
    @SpringBean
    private FiscalYearBudgetService fiscalYearBudgetService;
    @SpringBean
    private DepartmentService departmentService;
    @SpringBean
    protected SessionMetadataService sessionMetadataService;
    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    public EditFiscalYearBudgetPage(final PageParameters parameters) {
        super(parameters);
        jpaService = fiscalYearBudgetService;
        listPageClass = DepartmentOverviewPage.class;
    }

    public void checkInitParameters() {
        final Department department = sessionMetadataService.getSessionDepartment();
        final FiscalYear fiscalYear = sessionMetadataService.getSessionFiscalYear();

        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && (department == null || fiscalYear == null)) {
            logger.warn("Something wrong happened since we are trying to add a new ProcurementPlan Entity "
                    + "without having a department or a fiscalYear!");
            setResponsePage(StatusOverviewPage.class);
        }

        // safeguard that we are not creating multiple FiscalYearBudgets for the same department and fiscalYear
        if (entityId == null && fiscalYearBudgetService.countByDepartmentAndFiscalYear(department, fiscalYear) > 0) {
            logger.warn("We already have a FiscalYearBudget for the Department: "
                    + department + " and fiscalYear: " + fiscalYear);
            setResponsePage(StatusOverviewPage.class);
        }
    }

    @Override
    protected FiscalYearBudget newInstance() {
        final FiscalYearBudget entity = super.newInstance();

        entity.setDepartment(sessionMetadataService.getSessionDepartment());
        entity.setFiscalYear(sessionMetadataService.getSessionFiscalYear());

        return entity;
    }

    @Override
    protected void onInitialize() {
        checkInitParameters();
        super.onInitialize();
        editForm.add(new GenericSleepFormComponent<>("department"));
        editForm.add(new GenericSleepFormComponent<>("fiscalYear"));
        ComponentUtil.addBigDecimalField(editForm, "amountBudgeted");
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
                permissionEntityRenderableService.getAllowedAccess(this, editForm.getModelObject()));
    }
}
