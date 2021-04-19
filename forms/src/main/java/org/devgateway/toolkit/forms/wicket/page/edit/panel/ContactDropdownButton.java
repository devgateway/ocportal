package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonList;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author Octavian Ciubotaru
 */
public abstract class ContactDropdownButton<T extends AbstractContact<?>> extends GenericPanel<List<T>> {

    public ContactDropdownButton(String id, IModel<List<T>> model) {
        super(id, model);

        DropDownButton button = new DropDownButton("button", new StringResourceModel("selectContact")) {

            protected ButtonList newButtonList(final String markupId) {
                final ButtonList buttonList = new ButtonList(markupId,
                        new LoadableDetachableModel<List<AbstractLink>>() {
                            @Override
                            protected List<AbstractLink> load() {
                                return newSubMenuButtons(ButtonList.getButtonMarkupId());
                            }
                        });
                buttonList.setRenderBodyOnly(true);
                return buttonList;
            }

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                ArrayList<AbstractLink> links = new ArrayList<>();

                for (T contact : getModelObject()) {
                    links.add(new ContactLink(buttonMarkupId, Model.of(contact)) {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            ContactDropdownButton.this.onClick(target, getModel());
                        }
                    });
                }

                return links;
            }
        };
        button.setOutputMarkupPlaceholderTag(true);
        add(button);

        setOutputMarkupPlaceholderTag(true);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        setVisibilityAllowed(!ComponentUtil.isPrintMode() && !getModelObject().isEmpty());
    }

    public abstract void onClick(AjaxRequestTarget target, IModel<T> model);

    private abstract class ContactLink extends BootstrapAjaxLink<T> {

        ContactLink(String id, IModel<T> model) {
            super(id, model, Buttons.Type.Link);
        }

        @Override
        protected <L extends Serializable> Component newLabel(String markupId, IModel<L> model) {
            return new ContactLinkLabel(markupId, getModelObject().getDirectors(),
                    new StringJoiner(", ")
                            .add(getModelObject().getPhoneNumber())
                            .add(getModelObject().getEmail())
                            .add(getModelObject().getMailingAddress()).toString());
        }
    }

    private class ContactLinkLabel extends Fragment {

        ContactLinkLabel(String id, String line1, String line2) {
            super(id, "linkContent", ContactDropdownButton.this);
            add(new Label("line1", line1));
            add(new Label("line2", line2));
        }
    }
}
