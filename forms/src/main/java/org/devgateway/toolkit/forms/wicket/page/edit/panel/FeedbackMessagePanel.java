package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;

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
        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<FeedbackMessage> item) {
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
    }

    @Override
    protected boolean filterListItem(final FeedbackMessage contractDocuments) {
        return true;
    }
}