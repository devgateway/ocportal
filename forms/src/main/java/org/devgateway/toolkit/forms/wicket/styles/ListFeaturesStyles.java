package org.devgateway.toolkit.forms.wicket.styles;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author Octavian Ciubotaru
 */
public class ListFeaturesStyles extends CssResourceReference {

    public static final ListFeaturesStyles INSTANCE = new ListFeaturesStyles();

    public ListFeaturesStyles() {
        super(HomeStyles.class, "ListFeaturesStyles.css");
    }
}
