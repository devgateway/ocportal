package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.AbstractDateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.LocalDateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.LocalDateTextFieldConfig;
import org.apache.wicket.model.IModel;

import java.time.LocalDate;

/**
 * @author Nadejda Mandrescu
 */
public class LocalDateFieldBootstrapFormComponent extends AbstractDateFieldBootstrapFormComponent<LocalDate> {
    private static final long serialVersionUID = 7896197163912288823L;

    public LocalDateFieldBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                                final IModel<LocalDate> model) {
        super(id, labelModel, model);
    }

    public LocalDateFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    public LocalDateFieldBootstrapFormComponent(final String id, final IModel<LocalDate> model) {
        super(id, model);
    }

    @Override
    protected LocalDateTextFieldConfig newDateConfig() {
        return new LocalDateTextFieldConfig();
    }

    @Override
    protected LocalDateTextField newDateTextField(final String id, final AbstractDateTextFieldConfig config) {
        return new LocalDateTextField(id, initFieldModel(), (LocalDateTextFieldConfig) config);
    }
}
