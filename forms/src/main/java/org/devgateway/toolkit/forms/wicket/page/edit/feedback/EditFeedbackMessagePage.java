package org.devgateway.toolkit.forms.wicket.page.edit.feedback;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.FeedbackMessagePanel;
import org.devgateway.toolkit.forms.wicket.page.lists.feedback.ListFeedbackMessagePage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.devgateway.toolkit.web.rest.controller.alerts.AlertsEmailService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class EditFeedbackMessagePage extends AbstractEditPage<ReplyableFeedbackMessage> {

    @SpringBean
    private ReplyableFeedbackMessageService feedbackMessageService;

    @SpringBean
    private AlertsEmailService alertsEmailService;

    public EditFeedbackMessagePage(final PageParameters parameters) {
        super(parameters);

        jpaService = feedbackMessageService;
        listPageClass = ListFeedbackMessagePage.class;
    }

    @Override
    protected DeleteEditPageButton getDeleteEditPageButton() {
        DeleteEditPageButton deleteEditPageButton = super.getDeleteEditPageButton();
        deleteEditPageButton.setEnabled(false);
        return deleteEditPageButton;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(editForm, "name");
        name.required().setEnabled(false);

        TextFieldBootstrapFormComponent<String> email = ComponentUtil.addTextField(editForm, "email");
        email.required().getField().add(RfcCompliantEmailAddressValidator.getInstance()).setEnabled(false);

        TextAreaFieldBootstrapFormComponent<String> comment = ComponentUtil.addTextAreaField(editForm, "comment");
        comment.required().setEnabled(false);

        ComponentUtil.addCheckToggle(editForm, "visible");

        FeedbackMessagePanel feedbackMessagePanel = new FeedbackMessagePanel("replies");
        editForm.add(feedbackMessagePanel);
    }

    @Override
    protected void beforeSaveEntity(ReplyableFeedbackMessage saveable) {
        alertsEmailService.sendFeedbackAlertsForReplyable(saveable);
    }
}
