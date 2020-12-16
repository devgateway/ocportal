package org.devgateway.toolkit.forms.wicket.page.edit.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.EditorValidatorRoleAssignable;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.TitleAutogeneratable;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.MakueniEntityServiceResolver;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class EditAbstractMakueniEntityPage<T extends AbstractMakueniEntity>
        extends AbstractEditStatusEntityPage<T> implements EditorValidatorRoleAssignable {
    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractMakueniEntityPage.class);

    protected ButtonContentModal revertToDraftModal;

    @SpringBean
    protected SessionMetadataService sessionMetadataService;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    @SpringBean
    protected MakueniEntityServiceResolver makeniEntityServiceResolver;

    private Fragment extraStatusEntityButtons;

    protected TransparentWebMarkupContainer alertTerminated;
    protected FileInputBootstrapFormComponent formDocs;

    protected void checkInitParameters() {

    }

    public EditAbstractMakueniEntityPage() {
        this(new PageParameters());
    }

    public EditAbstractMakueniEntityPage(final PageParameters parameters) {
        super(parameters);

        this.listPageClass = DepartmentOverviewPage.class;
    }

    @Override
    protected boolean isViewMode() {
        return SecurityConstants.Action.VIEW
                .equals(permissionEntityRenderableService.getAllowedAccess(this, editForm.getModelObject()));
    }

    @Override
    protected void onAfterRevertToDraft(AjaxRequestTarget target) {
//       Collection<? extends AbstractMakueniEntity> allChildrenInHierarchy = getJpaService().getAllChildrenInHierarchy(
//                editForm.getModelObject());
//        allChildrenInHierarchy.stream().filter(c -> !c.getStatus().equals(DRAFT)).forEach(c -> {
//            logger.info("Reverting to DRAFT " + c.getClass().getSimpleName());
//            c.setNewStatusComment("Reverted to DRAFT because of related "
//                    + editForm.getModelObject().getClass().getSimpleName() + " entity was reverted to DRAFT");
//            final StatusChangedComment comment = new StatusChangedComment();
//            comment.setStatus(DRAFT);
//            comment.setComment(c.getNewStatusComment());
//            c.getStatusComments().add(comment);
//            c.setStatus(DRAFT);
//            makeniEntityServiceResolver.saveAndFlushMakueniEntity(c);
//        });
    }

    protected ButtonContentModal createRevertToDraftModal() {
        ButtonContentModal buttonContentModal = new ButtonContentModal(
                "revertToDraftModal",
                new StringResourceModel("confirmRevertToDraftModal.content", this),
                new StringResourceModel("confirmRevertToDraftModal.rejectToDraft", this), Buttons.Type.Warning);
        return buttonContentModal;
    }

    @Override
    protected void beforeSaveEntity(T saveable) {
        super.beforeSaveEntity(saveable);

        if (saveable instanceof TitleAutogeneratable) {
            ((TitleAutogeneratable) saveable).autogenerateTitleWhenEmpty();
        }
    }

    @Override
    protected SaveEditPageButton getRevertToDraftPageButton() {
        final SaveEditPageButton saveEditPageButton = new SaveEditPageButton("revertToDraft",
                new StringResourceModel("revertToDraft", this, null)) {
            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                onBeforeRevertToDraft(target);
                setStatusAppendComment(DBConstants.Status.DRAFT);
                super.onSubmit(target);
                target.add(editForm);
                onAfterRevertToDraft(target);
            }
        };
        saveEditPageButton.setIconType(FontAwesomeIconType.thumbs_down);
        return saveEditPageButton;
    }

    protected void onBeforeRevertToDraft(AjaxRequestTarget target) {

    }


    public AbstractMakueniEntityService<T> getJpaService() {
        return (AbstractMakueniEntityService<T>) jpaService;
    }

    @Override
    protected void onInitialize() {
        checkInitParameters();
        super.onInitialize();

        if (permissionEntityRenderableService.getAllowedAccess(this, editForm.getModelObject()) == null) {
            setResponsePage(listPageClass);
        }


        alertTerminated = new TransparentWebMarkupContainer("alertTerminated");
        alertTerminated.setVisibilityAllowed(false);
        editForm.add(alertTerminated);

        enableDisableAutosaveFields();

//        extraStatusEntityButtons = new Fragment("extraStatusEntityButtons", "extraStatusButtons", this);
//        entityButtonsFragment.replace(extraStatusEntityButtons);
//        extraStatusEntityButtons.add(revertToDraftModal);

    }

    @Override
    protected void addSaveButtonsPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getCommaCombinedRoles());
        button.setVisibilityAllowed(!isDisableEditingEvent());
    }

    @Override
    protected void addTerminateButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getValidatorRole());
        if (editForm.getModelObject().isNew()) {
            button.setVisibilityAllowed(false);
        }
    }

    @Override
    protected void addApproveButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(
                button, Component.RENDER, getValidatorRole());
        button.setVisibilityAllowed(button.isVisibilityAllowed()
                && DBConstants.Status.SUBMITTED.equals(editForm.getModelObject().getStatus()));
    }

    @Override
    protected void addSaveRevertButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getValidatorRole());
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getCommaCombinedRoles());
        button.setVisibilityAllowed(button.isVisibilityAllowed()
                && !DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus()));

        // additionally normal users should not revert anything that was already validated
        if (FormSecurityUtil.isCurrentRoleOnlyUser(getUserRole(), getValidatorRole())
                && DBConstants.Status.APPROVED.equals(editForm.getModelObject().getStatus())) {
            button.setVisibilityAllowed(false);
        } else

            //admins can revert anything, including terminated, but only on the terminated form, not elsewhere!
            if (FormSecurityUtil.isCurrentUserAdmin()
                    && ((!isTerminated() && DBConstants.Status.APPROVED.equals(editForm.getModelObject().getStatus()))
                    || DBConstants.Status.TERMINATED.equals(editForm.getModelObject().getStatus()))) {
                button.setVisibilityAllowed(true);
            }
    }

    @Override
    protected void addDeleteButtonPermissions(final Component button) {
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);
        button.setVisibilityAllowed(entityId != null && !isTerminated() && !isViewMode());
        MetaDataRoleAuthorizationStrategy.authorize(
                button, Component.RENDER, getCommaCombinedRoles());
    }

    protected FileInputBootstrapFormComponent addFormDocs() {
        formDocs = new FileInputBootstrapFormComponent("formDocs");
        editForm.add(formDocs);
        return formDocs;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
                BaseStyles.class, "assets/js/formLeavingHelper.js")));
    }
}
