/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.edit;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxYesNoToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.StatusChangedComment;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.select2.Select2Choice;

/**
 * @author mpostelnicu Page used to make editing easy, extend to get easy access
 * to one entity for editing
 */
public abstract class AbstractEditStatusEntityPage<T extends AbstractStatusAuditableEntity>
        extends AbstractEditPage<T> {
    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    private Fragment entityButtonsFragment;

    private SaveEditPageButton saveSubmitButton;

    private SaveEditPageButton saveValidateButton;

    private SaveEditPageButton saveContinueButton;

    private SaveEditPageButton revertToDraftPageButton;

    private CheckBoxYesNoToggleBootstrapFormComponent visibleStatusComments;

    private TransparentWebMarkupContainer comments;

    private ListView<StatusChangedComment> statusComments;

    private TextAreaFieldBootstrapFormComponent<String> newStatusComment;

    private Label statusLabel;

    private String previousStatus;

    private Label autoSaveLabel;

    private HiddenField<Double> verticalPosition;

    private HiddenField<Double> maxHeight;

    public AbstractEditStatusEntityPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()) == null) {
            setResponsePage(listPageClass);
        }

        addAutosaveLabel();
        addVerticalMaxPositionFields();

        statusLabel = addStatusLabel();
        editForm.add(statusLabel);

        visibleStatusComments = getVisibleStatusComments();
        editForm.add(visibleStatusComments);

        comments = new TransparentWebMarkupContainer("comments");
        comments.setOutputMarkupId(true);
        comments.setOutputMarkupPlaceholderTag(true);
        comments.setVisibilityAllowed(false);
        editForm.add(comments);

        statusComments = getStatusCommentsListView();
        newStatusComment = getNewStatusCommentField();
        comments.add(statusComments);
        comments.add(newStatusComment);

        entityButtonsFragment = new Fragment("extraButtons", "entityButtons", this);
        editForm.replace(entityButtonsFragment);

        saveSubmitButton = getSaveSubmitPageButton();
        entityButtonsFragment.add(saveSubmitButton);

        saveValidateButton = getSaveValidatePageButton();
        entityButtonsFragment.add(saveValidateButton);

        saveContinueButton = getSaveDraftAndContinueButton();
        entityButtonsFragment.add(saveContinueButton);

        revertToDraftPageButton = getRevertToDraftPageButton();
        entityButtonsFragment.add(revertToDraftPageButton);

        applyDraftSaveBehavior(saveButton);
        applyDraftSaveBehavior(saveContinueButton);
        applyDraftSaveBehavior(revertToDraftPageButton);

        setButtonsPermissions();
    }

    @Override
    protected void afterSaveEntity(final T saveable) {
        getPageParameters().set(WebConstants.V_POSITION, verticalPosition.getValue())
                .set(WebConstants.MAX_HEIGHT, maxHeight.getValue());
    }

    @Override
    protected void afterLoad(final IModel<T> entityModel) {
        super.afterLoad(entityModel);
        previousStatus = entityModel.getObject().getStatus();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        checkAndSendEventForDisableEditing();

        this.statusLabel.setVisibilityAllowed(editForm.getModelObject().getVisibleStatusLabel());
    }

    private boolean isViewMode() {
        return SecurityConstants.Action.VIEW.equals(
                permissionEntityRenderableService.getAllowedAccess(editForm.getModelObject()));
    }

    private void checkAndSendEventForDisableEditing() {
        if (!Strings.isEqual(editForm.getModelObject().getStatus(), DBConstants.Status.DRAFT) || isViewMode()) {
            send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());
        }
    }

    private void addAutosaveLabel() {
        autoSaveLabel = new Label("autoSaveLabel", new StringResourceModel("autoSaveLabelMessage", this)
                .setParameters(settingsUtils.getAutosaveTime()));
        autoSaveLabel.setVisibilityAllowed(false);
        autoSaveLabel.setOutputMarkupPlaceholderTag(true);
        autoSaveLabel.setOutputMarkupId(true);
        editForm.add(autoSaveLabel);
    }

    private void addVerticalMaxPositionFields() {
        verticalPosition = new HiddenField<>("verticalPosition", new Model<>(), Double.class);
        verticalPosition.setOutputMarkupId(true);
        editForm.add(verticalPosition);

        maxHeight = new HiddenField<>("maxHeight", new Model<>(), Double.class);
        maxHeight.setOutputMarkupId(true);
        editForm.add(maxHeight);
    }

    protected void enableDisableAutosaveFields(final AjaxRequestTarget target) {
        addAutosaveBehavior(target);

        saveButton.setEnabled(true);
        saveContinueButton.setEnabled(true);
        saveSubmitButton.setEnabled(true);

        if (target != null) {
            target.add(saveButton, saveSubmitButton, saveContinueButton);
        }
    }

    private void addAutosaveBehavior(final AjaxRequestTarget target) {
        // enable autosave
        if (!ComponentUtil.isViewMode()
                && Strings.isEqual(editForm.getModelObject().getStatus(), DBConstants.Status.DRAFT)) {
            saveContinueButton.add(getAutosaveBehavior());
            autoSaveLabel.setVisibilityAllowed(true);
            if (target != null) {
                target.add(autoSaveLabel);
            }
        }
    }

    private AbstractAjaxTimerBehavior getAutosaveBehavior() {
        final AbstractAjaxTimerBehavior ajaxTimerBehavior = new AbstractAjaxTimerBehavior(
                Duration.minutes(settingsUtils.getAutosaveTime())) {
            @Override
            protected void onTimer(final AjaxRequestTarget target) {
                // display block UI message until the page is reloaded
                target.prependJavaScript(getShowBlockUICode());

                // disable all fields from js and lose focus (execute this javascript code before components processed)
                target.prependJavaScript("$(document.activeElement).blur();");

                // invoke autosave from js (execute this javascript code before components processed)
                target.prependJavaScript(
                        "$('#" + maxHeight.getMarkupId() + "').val($(document).height()); "
                                + "$('#" + verticalPosition.getMarkupId() + "').val($(window).scrollTop()); "
                                + "$('#" + saveContinueButton.getMarkupId() + "').click();");

                // disable all buttons from js
                target.prependJavaScript("$('#" + editForm.getMarkupId() + " button').prop('disabled', true);");
            }
        };

        return ajaxTimerBehavior;
    }

    private Label addStatusLabel() {
        statusLabel = new Label("statusLabel", editForm.getModelObject().getStatus());
        statusLabel.add(new AttributeModifier("class", new Model<>("label " + getStatusLabelClass())));
        statusLabel.setVisibilityAllowed(editForm.getModelObject().getVisibleStatusLabel());
        return statusLabel;
    }

    private String getStatusLabelClass() {
        if (editForm.getModelObject().getStatus() == null) {
            return "";
        }

        switch (editForm.getModelObject().getStatus()) {
            case DBConstants.Status.VALIDATED:
                return "label-success";
            case DBConstants.Status.DRAFT:
                return "label-danger";
            case DBConstants.Status.SUBMITTED:
                return "label-warning";
            default:
                return "";
        }
    }

    private CheckBoxYesNoToggleBootstrapFormComponent getVisibleStatusComments() {
        final CheckBoxYesNoToggleBootstrapFormComponent checkBoxBootstrapFormComponent =
                new CheckBoxYesNoToggleBootstrapFormComponent("visibleStatusComments") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        comments.setVisibilityAllowed(editForm.getModelObject().getVisibleStatusComments());
                        target.add(comments);
                    }

                    @Override
                    public void onEvent(final IEvent<?> event) {
                        // do nothing - keep this field enabled
                    }
                };
        checkBoxBootstrapFormComponent.setVisibilityAllowed(!isViewMode());
        return checkBoxBootstrapFormComponent;
    }

    private TextAreaFieldBootstrapFormComponent<String> getNewStatusCommentField() {
        final TextAreaFieldBootstrapFormComponent<String> comment =
                new TextAreaFieldBootstrapFormComponent<String>("newStatusComment") {
                    @Override
                    public void onEvent(final IEvent<?> event) {
                        // do nothing - keep this field enabled
                    }
                };
        comment.setShowTooltip(true);
        return comment;
    }

    private ListView<StatusChangedComment> getStatusCommentsListView() {
        final ListView<StatusChangedComment> statusComments = new ListView<StatusChangedComment>("statusComments") {
            @Override
            protected void populateItem(final ListItem<StatusChangedComment> item) {
                item.setModel(new CompoundPropertyModel<>(item.getModel()));
                item.add(new Label("commentIdx", item.getIndex()));
                item.add(new Label("status"));
                item.add(new Label("comment"));
                item.add(new Label("createdBy", item.getModelObject().getCreatedBy().get()));
                item.add(DateLabel.forDateStyle("created",
                        Model.of(ComponentUtil.getDateFromLocalDate(
                                item.getModelObject().getCreatedDate().get().toLocalDate())), "SS"
                ));
            }
        };
        statusComments.setReuseItems(true);
        statusComments.setOutputMarkupId(true);

        return statusComments;
    }

    /**
     * Use this function to get the block UI message while the form is saved.
     */
    private String getShowBlockUICode() {
        return "blockUI('" + new StringResourceModel("autosave_message", AbstractEditStatusEntityPage.this, null)
                .getString() + "')";
    }

    /*******************************************************************************
     * Buttons Behavior
     *******************************************************************************/
    private void applyDraftSaveBehavior(final BootstrapSubmitButton button) {
        // disable form validation
        button.setDefaultFormProcessing(false);
    }

    @Override
    protected SaveEditPageButton getSaveEditPageButton() {
        final SaveEditPageButton button = new SaveEditPageButton("save",
                new StringResourceModel("saveButton", this, null)) {

            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                editForm.visitChildren(
                        GenericBootstrapFormComponent.class, new AllowNullForCertainInvalidFieldsVisitor());
                setStatusAppendComment(DBConstants.Status.DRAFT);
                super.onSubmit(target);
            }
        };

        return button;
    }

    private SaveEditPageButton getSaveSubmitPageButton() {
        final SaveEditPageButton button = new SaveEditPageButton("saveSubmit",
                new StringResourceModel("saveSubmit", this, null)) {
            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setStatusAppendComment(DBConstants.Status.SUBMITTED);
                super.onSubmit(target);
            }
        };

        button.setIconType(FontAwesomeIconType.send);
        return button;
    }

    private SaveEditPageButton getSaveDraftAndContinueButton() {
        final SaveEditPageButton button = new SaveEditPageButton("saveContinue",
                new StringResourceModel("saveContinue", this, null)) {

            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                editForm.visitChildren(
                        GenericBootstrapFormComponent.class, new AllowNullForCertainInvalidFieldsVisitor());
                setStatusAppendComment(DBConstants.Status.DRAFT);
                super.onSubmit(target);
            }

            @Override
            protected Class<? extends BasePage> getResponsePage() {
                return (Class<? extends BasePage>) getPage().getClass();
            }

            @Override
            protected PageParameters getParameterPage() {
                return getPageParameters();
            }
        };

        button.setIconType(FontAwesomeIconType.tasks);
        return button;
    }

    private SaveEditPageButton getSaveValidatePageButton() {
        final SaveEditPageButton saveEditPageButton = new SaveEditPageButton("validate",
                new StringResourceModel("validate", this, null)) {

            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setStatusAppendComment(DBConstants.Status.VALIDATED);
                super.onSubmit(target);
            }
        };
        saveEditPageButton.setIconType(FontAwesomeIconType.thumbs_up);
        return saveEditPageButton;
    }


    private SaveEditPageButton getRevertToDraftPageButton() {
        final SaveEditPageButton saveEditPageButton = new SaveEditPageButton("revertToDraft",
                new StringResourceModel("revertToDraft", this, null)) {
            @Override
            protected String getOnClickScript() {
                return WebConstants.DISABLE_FORM_LEAVING_JS;
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                setStatusAppendComment(DBConstants.Status.DRAFT);
                super.onSubmit(target);
                target.add(editForm);
                setButtonsPermissions();
            }
        };
        saveEditPageButton.setIconType(FontAwesomeIconType.thumbs_down);
        return saveEditPageButton;
    }

    private void setStatusAppendComment(final String status) {
        final T saveable = editForm.getModelObject();

        // do not save an empty comment if previous status is same as current status and comment box is empty
        if (status.equals(saveable.getStatus()) && ObjectUtils.isEmpty(saveable.getNewStatusComment())) {
            saveable.setStatus(status);
            return;
        }
        saveable.setStatus(status);

        final StatusChangedComment comment = new StatusChangedComment();
        comment.setStatus(status);
        comment.setComment(editForm.getModelObject().getNewStatusComment());
        saveable.getStatusComments().add(comment);
    }

    private void setButtonsPermissions() {
        addSaveButtonsPermissions(saveButton);
        addSaveButtonsPermissions(saveContinueButton);
        addSaveButtonsPermissions(saveSubmitButton);
        addValidateButtonPermissions(saveValidateButton);
        addSaveRevertButtonPermissions(revertToDraftPageButton);
        addDeleteButtonPermissions(deleteButton);

        // no need to display the buttons on print view so we overwrite the above permissions
        if (ComponentUtil.isViewMode()) {
            saveContinueButton.setVisibilityAllowed(false);
            saveSubmitButton.setVisibilityAllowed(false);
            saveValidateButton.setVisibilityAllowed(false);
            revertToDraftPageButton.setVisibilityAllowed(false);
        }
    }

    private void addDefaultAllButtonsPermissions(final Component button) {
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);
        button.setVisibilityAllowed(!isViewMode());
    }

    private void addSaveButtonsPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        button.setVisibilityAllowed(
                DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus())
                        && !isViewMode());
    }

    private void addValidateButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_VALIDATOR);
        button.setVisibilityAllowed(
                DBConstants.Status.SUBMITTED.equals(editForm.getModelObject().getStatus())
                        && !isViewMode());
    }

    private void addSaveRevertButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_VALIDATOR);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        button.setVisibilityAllowed(
                !DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus())
                        && !isViewMode());

        // additionally normal users should not revert anything that was already validated
        if (WebSecurityUtil.isCurrentRoleUser()
                && DBConstants.Status.VALIDATED.equals(editForm.getModelObject().getStatus())) {
            button.setVisibilityAllowed(false);
        }
    }

    private void addDeleteButtonPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
    }

    private void scrollToPreviousPosition(final IHeaderResponse response) {
        response.render(
                OnDomReadyHeaderItem.forScript(String.format(
                        "var vPosition= +%s, mHeight = +%s, cmHeight=$(document).height();"
                                + "if(mHeight!=0) $(window).scrollTop(vPosition*cmHeight/mHeight)",
                        getPageParameters().get(WebConstants.V_POSITION).toDouble(0),
                        getPageParameters().get(WebConstants.MAX_HEIGHT).toDouble(0)

                )));
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        scrollToPreviousPosition(response);
    }

    /**
     * Allow null saving for draft entities even if the field is required.
     * Bypass validation for this purpose.
     *
     * @author mpostelnicu
     */
    public class AllowNullForCertainInvalidFieldsVisitor
            implements IVisitor<GenericBootstrapFormComponent<?, ?>, Void> {
        @Override
        public void component(final GenericBootstrapFormComponent<?, ?> object, final IVisit<Void> visit) {
            // we found the GenericBootstrapFormComponent, stop doing useless
            // things like traversing inside the GenericBootstrapFormComponent itself
            visit.dontGoDeeper();

            // do not process disabled fields
            if (!object.isEnabledInHierarchy()) {
                return;
            }
            object.getField().processInput();

            // we try validate the field
            object.getField().validate();

            // still, if the field is invalid, its input is null, and field is
            // of a certain type, we turn
            // the input into a null. This helps us to save empty REQUIRED
            // fields when saving as draft
            if (!object.getField().isValid() && Strings.isEmpty(object.getField().getInput())) {
                // for text/select fields we just make the object model null
                if (object.getField() instanceof TextField<?> || object.getField() instanceof TextArea<?>
                        || object.getField() instanceof Select2Choice<?>) {
                    object.getField().getModel().setObject(null);
                }

            }
        }
    }
}
