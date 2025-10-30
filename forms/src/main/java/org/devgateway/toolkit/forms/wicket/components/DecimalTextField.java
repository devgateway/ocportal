package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;

/**
 * A TextField for decimal numbers extending Wicket's NumberTextField<Double>
 *
 * @author mpostelnicu
 */
public class DecimalTextField extends NumberTextField<Double> {

    private static final long serialVersionUID = 1L;

    public DecimalTextField(String id, IModel<Double> model) {
        super(id, model, Double.class);
    }

}