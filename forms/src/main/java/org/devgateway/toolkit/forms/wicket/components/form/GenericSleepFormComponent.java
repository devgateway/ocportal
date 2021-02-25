package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.fm.DgFmFormComponentSubject;
import org.devgateway.toolkit.forms.models.ViewModeConverterModel;
import org.devgateway.toolkit.forms.wicket.components.FieldPanel;

/**
 * @author idobre
 * @since 2019-04-10
 */
public class GenericSleepFormComponent<T> extends FieldPanel<T> implements DgFmFormComponentSubject {
    public GenericSleepFormComponent(final String id) {
        super(id);
    }

    public GenericSleepFormComponent(final String id, final IModel<T> model) {
        super(id, model);
    }

    @Override
    public boolean isEnabled() {
        return isFmEnabled(super::isEnabled);
    }

    @Override
    public boolean isVisible() {
        return isFmVisible(super::isVisible);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final IModel<String> labelModel = new ResourceModel(getId() + ".label");
        add(new Label("label", labelModel));

        add(newValueComponent("value"));
    }

    protected Component newValueComponent(String id) {
        Label value = new Label(id, new ViewModeConverterModel<>(getModel()));
        value.setEscapeModelStrings(false);
        return value;
    }
}
