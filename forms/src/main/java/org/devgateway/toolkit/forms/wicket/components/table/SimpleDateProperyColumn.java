package org.devgateway.toolkit.forms.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @param <T>
 * @param <R>
 * @author mpostelnicu
 */
public class SimpleDateProperyColumn<T, R extends ChronoZonedDateTime<LocalDate>> extends PropertyColumn<T, String> {

    private final String dateFormat;
    private SerializableFunction<T, R> dateGetter;

    public SimpleDateProperyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression,
                                   SerializableFunction<T, R> dateGetter) {
        this(displayModel, sortProperty, propertyExpression, dateGetter, DBConstants.DATE_FORMAT);
    }

    public SimpleDateProperyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression,
                                   SerializableFunction<T, R> dateGetter, String dateFormat) {
        super(displayModel, sortProperty, propertyExpression);
        this.dateGetter = dateGetter;
        this.dateFormat = dateFormat;
    }
    
    @Override
    public void populateItem(final Item<ICellPopulator<T>> item,
                             final String componentId,
                             final IModel<T> rowModel) {
        R dateTime = dateGetter.apply(rowModel.getObject());
        if (!ObjectUtils.isEmpty(dateTime)) {
            item.add(new Label(componentId, dateTime.format(DateTimeFormatter.ofPattern(dateFormat))));
        } else {
            item.add(new Label(componentId, ""));
        }
    }

}
