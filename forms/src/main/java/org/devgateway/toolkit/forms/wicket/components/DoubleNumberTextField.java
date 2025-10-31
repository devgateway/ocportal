package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;

/**
 * A TextField for double numbers extending Wicket's NumberTextField<Double>
 *
 * @author mpostelnicu
 */
public class DoubleNumberTextField extends NumberTextField<Double> {

    private static final long serialVersionUID = 1L;

    public DoubleNumberTextField(String id, IModel<Double> model) {
        super(id, model, Double.class);
    }
}
