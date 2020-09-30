package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.web.security.SecurityUtil;

public class FeedbackMessagePanel extends ListViewSectionPanel<FeedbackMessage, ReplyableFeedbackMessage> {


    public FeedbackMessagePanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public FeedbackMessage createNewChild(final IModel<ReplyableFeedbackMessage> parentModel) {
        final FeedbackMessage child = new FeedbackMessage();
        child.setParent(parentModel.getObject());
        Person currentUser = SecurityUtil.getCurrentAuthenticatedPerson();
        child.setEmail(currentUser.getEmail());
        child.setName(currentUser.getFirstName() + " " + currentUser.getLastName());
        return child;
    }

    @Override
    protected BootstrapDeleteButton getRemoveChildButton(FeedbackMessage item,
                                                         NotificationPanel removeButtonNotificationPanel) {
        BootstrapDeleteButton removeChildButton = super.getRemoveChildButton(item, removeButtonNotificationPanel);
        removeChildButton.setEnabled(!item.isAddedByPublic());
        return removeChildButton;
    }

    @Override
    public void populateCompoundListItem(final ListItem<FeedbackMessage> item) {


        Label addedBy = new Label("addedBy", new StringResourceModel("addedBy", this));
        addedBy.setVisibilityAllowed(item.getModelObject().isAddedByPublic());
        item.add(addedBy);

        TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(item, "name");
        name.required();
        item.add(name);

        TextFieldBootstrapFormComponent<String> email = ComponentUtil.addTextField(item, "email");
        email.required().getField().add(RfcCompliantEmailAddressValidator.getInstance());
        item.add(email);

        TextAreaFieldBootstrapFormComponent<String> comment = ComponentUtil.addTextAreaField(item, "comment");
        comment.required();
        item.add(comment);

        CheckBoxToggleBootstrapFormComponent visible = ComponentUtil.addCheckToggle(item, "visible");
        item.add(visible);

        name.setEnabled(false);
        email.setEnabled(false);
        if (item.getModelObject().isAddedByPublic()) {
            comment.setEnabled(false);
        }
    }

    @Override
    protected boolean filterListItem(final FeedbackMessage contractDocuments) {
        return true;
    }
}