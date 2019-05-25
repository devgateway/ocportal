package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.models.ViewModeConverterModel;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;

/**
 * @author idobre
 * @since 2019-04-10
 */
public class GenericSleepFormComponent<T> extends FieldPanel<T> {
    public GenericSleepFormComponent(final String id) {
        super(id);
    }

    public GenericSleepFormComponent(final String id, final IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final IModel<String> labelModel = new ResourceModel(getId() + ".label");
        add(new Label("label", labelModel));

        final Label value = new Label("value", new ViewModeConverterModel<>(getModel()));
        value.setEscapeModelStrings(false);
        add(value);
    }
}
