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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.EditorValidatorRoleAssignable;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.StatusChangedComment;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.TitleAutogeneratable;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.MakueniEntityServiceResolver;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;

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
    public boolean isDisableEditingEvent() {
        return !Strings.isEqual(editForm.getModelObject().getStatus(), DBConstants.Status.DRAFT) || isViewMode();
    }

    @Override
    protected void onAfterRevertToDraft(AjaxRequestTarget target) {
        Collection<? extends AbstractMakueniEntity> allChildrenInHierarchy = getJpaService().getAllChildrenInHierarchy(
                editForm.getModelObject());
        allChildrenInHierarchy.stream().filter(c -> !c.getStatus().equals(DRAFT)).forEach(c -> {
            logger.info("Reverting to DRAFT " + c.getClass().getSimpleName());
            c.setNewStatusComment("Reverted to DRAFT because of related "
                    + editForm.getModelObject().getClass().getSimpleName() + " entity was reverted to DRAFT");
            final StatusChangedComment comment = new StatusChangedComment();
            comment.setStatus(DRAFT);
            comment.setComment(c.getNewStatusComment());
            c.getStatusComments().add(comment);
            c.setStatus(DRAFT);
            makeniEntityServiceResolver.saveAndFlushMakueniEntity(c);
        });
    }

    protected ButtonContentModal createRevertToDraftModal() {
        ButtonContentModal buttonContentModal = new ButtonContentModal(
                "revertToDraftModal",
                Model.of("Rejecting to draft this entity will result in rejecting to draft"
                        + " any other entities downstream. Proceed?"),
                Model.of("REJECT TO DRAFT"), Buttons.Type.Warning);
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
    protected ModalSaveEditPageButton getRevertToDraftPageButton() {
        revertToDraftModal = createRevertToDraftModal();
        final ModalSaveEditPageButton saveEditPageButton = new ModalSaveEditPageButton("revertToDraft",
                new StringResourceModel("revertToDraft", this, null), revertToDraftModal) {

            @Override
            public void continueSubmit(AjaxRequestTarget target) {
                setStatusAppendComment(DBConstants.Status.DRAFT);
                super.continueSubmit(target);
                target.add(editForm);
                onAfterRevertToDraft(target);
            }
        };
        revertToDraftModal.modalSavePageButton(saveEditPageButton);
        saveEditPageButton.setIconType(FontAwesomeIconType.thumbs_down);
        return saveEditPageButton;
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

        enableDisableAutosaveFields(null);

        extraStatusEntityButtons = new Fragment("extraStatusEntityButtons", "extraStatusButtons", this);
        entityButtonsFragment.replace(extraStatusEntityButtons);
        extraStatusEntityButtons.add(revertToDraftModal);


    }

    @Override
    protected void addSaveButtonsPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getUserRole());
        button.setVisibilityAllowed(button.isVisibilityAllowed()
                && DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus()));
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
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, getUserRole());
        button.setVisibilityAllowed(button.isVisibilityAllowed()
                && !DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus()));

        // additionally normal users should not revert anything that was already validated
        if (WebSecurityUtil.isCurrentRoleOnlyUser(getUserRole(), getValidatorRole())
                && DBConstants.Status.APPROVED.equals(editForm.getModelObject().getStatus())) {
            button.setVisibilityAllowed(false);
        } else

            //admins can revert anything, including terminated, but only on the terminated form, not elsewhere!
            if (WebSecurityUtil.isCurrentUserAdmin()
                    && ((!isTerminated() && DBConstants.Status.APPROVED.equals(editForm.getModelObject().getStatus()))
                    || DBConstants.Status.TERMINATED.equals(editForm.getModelObject().getStatus()))) {
                button.setVisibilityAllowed(true);
            }
    }

    @Override
    protected void addDeleteButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(
                button, Component.RENDER, getUserRole());
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
                BaseStyles.class, "assets/js/formLeavingHelper.js")));
    }
}
