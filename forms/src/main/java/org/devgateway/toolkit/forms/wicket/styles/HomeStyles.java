package org.devgateway.toolkit.forms.wicket.styles;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author idobre
 * @since 2019-05-17
 */
public class HomeStyles extends CssResourceReference {
    public static final HomeStyles INSTANCE = new HomeStyles();

    public HomeStyles() {
        super(HomeStyles.class, "HomeStyles.css");
    }
}
