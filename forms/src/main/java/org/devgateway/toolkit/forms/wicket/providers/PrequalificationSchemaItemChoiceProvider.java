package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;

/**
 * @author Octavian Ciubotaru
 */
public class PrequalificationSchemaItemChoiceProvider
        extends EntityListChoiceProvider<PrequalificationSchemaItem> {

    private final IModel<PrequalificationYearRange> yearRange;

    public PrequalificationSchemaItemChoiceProvider(IModel<PrequalificationYearRange> yearRange) {
        super(yearRange.map(PrequalificationYearRange::getSchema)
                .map(PrequalificationSchema::getItems));
        this.yearRange = yearRange;
    }

    @Override
    public String getDisplayValue(PrequalificationSchemaItem object) {
        return object.toString(yearRange.getObject());
    }
}
