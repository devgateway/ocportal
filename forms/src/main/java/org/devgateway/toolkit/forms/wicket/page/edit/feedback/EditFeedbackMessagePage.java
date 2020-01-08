package org.devgateway.toolkit.forms.wicket.page.edit.feedback;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.feedback.ListFeedbackMessagePage;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.FeedbackMessageService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class EditFeedbackMessagePage extends AbstractEditPage<FeedbackMessage> {

    @SpringBean
    private FeedbackMessageService feedbackMessageService;

    public EditFeedbackMessagePage(final PageParameters parameters) {
        super(parameters);

        jpaService = feedbackMessageService;
        listPageClass = ListFeedbackMessagePage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(editForm, "name");
        name.required();

        TextFieldBootstrapFormComponent<String> email = ComponentUtil.addTextField(editForm, "email");
        email.required().getField().add(RfcCompliantEmailAddressValidator.getInstance());
        TextAreaFieldBootstrapFormComponent<String> comment = ComponentUtil.addTextAreaField(editForm, "comment");
        comment.required();

    }
}
