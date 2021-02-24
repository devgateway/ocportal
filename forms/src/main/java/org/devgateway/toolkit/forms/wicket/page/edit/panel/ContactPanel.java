package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.devgateway.toolkit.forms.wicket.components.CompoundSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;

/**
 * @author Octavian Ciubotaru
 */
public class ContactPanel<T extends AbstractContact<?>> extends CompoundSectionPanel<T> {

    public ContactPanel(String id) {
        super(id);
    }

    public ContactPanel(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextFieldBootstrapFormComponent<String> directors;
        directors = new TextFieldBootstrapFormComponent<String>("directors") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                ContactPanel.this.onUpdate(target);
            }
        };
        directors.required();
        add(directors);

        TextFieldBootstrapFormComponent<String> email;
        email = new TextFieldBootstrapFormComponent<String>("email") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                ContactPanel.this.onUpdate(target);
            }
        };
        email.required();
        email.getField().add(EmailAddressValidator.getInstance());
        add(email);

        TextFieldBootstrapFormComponent<String> phoneNumber;
        phoneNumber = new TextFieldBootstrapFormComponent<String>("phoneNumber") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                ContactPanel.this.onUpdate(target);
            }
        };
        phoneNumber.required();
        add(phoneNumber);

        TextFieldBootstrapFormComponent<String> mailingAddress;
        mailingAddress = new TextFieldBootstrapFormComponent<String>("mailingAddress") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                ContactPanel.this.onUpdate(target);
            }
        };
        mailingAddress.required();
        add(mailingAddress);
    }

    public void clearInput() {
        visitChildren(FormComponent.class, (IVisitor<FormComponent<?>, Void>) (object, visit) -> {
            object.clearInput();
            visit.dontGoDeeper();
        });
    }

    protected void onUpdate(AjaxRequestTarget target) {
    }
}
