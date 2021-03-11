package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;

/**
 * @author Octavian Ciubotaru
 */
public class StyledLambdaColumn<T, S> extends LambdaColumn<T, S> {

    private final String cssClass;

    public StyledLambdaColumn(final IModel<String> displayModel, String cssClass,
            final SerializableFunction<T, ?> function) {
        this(displayModel, cssClass, null, function);
    }

    public StyledLambdaColumn(final IModel<String> displayModel, String cssClass, final S sortProperty,
            final SerializableFunction<T, ?> function) {
        super(displayModel, sortProperty, function);
        this.cssClass = cssClass;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }
}
