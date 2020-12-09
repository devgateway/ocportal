package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.InputBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class RadioGroupBootstrapFormComponent<T extends Serializable>
        extends GenericEnablingBootstrapFormComponent<T, RadioGroup<T>> {

    private IModel<List<T>> options;

    public RadioGroupBootstrapFormComponent(String id, List<T> options) {
        super(id);
        this.options.setObject(options);
    }

    @Override
    protected boolean boundComponentsVisibilityAllowed(T selectedValue) {
        return false;
    }

    @Override
    protected RadioGroup<T> inputField(String id, IModel<T> model) {
        options = Model.ofList(new ArrayList<>());

        RadioGroup<T> radioGroup = new RadioGroup<>(id, initFieldModel());

        ListView<T> radios = new ListView<T>("radios", options) {
            @Override
            protected void populateItem(ListItem<T> item) {
                item.add(new Radio<>("radio", item.getModel(), radioGroup));
                item.add(new Label("label", item.getModel()));
            }
        };

        radioGroup.add(radios);

        return radioGroup;
    }

    @Override
    protected InputBehavior getInputBehavior() {
        return null;
    }
}
