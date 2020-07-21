package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.form.LocalDateFieldBootstrapFormComponent;

import java.time.LocalDate;


public class LocalDateFilteredBootstrapPropertyColumn<T, S> extends TextFilteredPropertyColumn<T, LocalDate, S> {
    private static final long serialVersionUID = -222944168709002277L;

    public LocalDateFilteredBootstrapPropertyColumn(final IModel<String> displayModel, final S sortProperty,
            final String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    public LocalDateFilteredBootstrapPropertyColumn(final IModel<String> displayModel,
                                                    final String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public Component getFilter(final String componentId, final FilterForm<?> form) {
        final LocalDateFieldBootstrapFormComponent dateField =
                new LocalDateFieldBootstrapFormComponent(componentId, getFilterModel(form));
        dateField.hideLabel();
        dateField.getField().add(AttributeModifier.replace("onchange", "this.form.submit();"));
        return dateField;
    }
}

