package org.devgateway.toolkit.forms.wicket.styles;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author idobre
  */
public class OverviewStyles extends CssResourceReference {
    private static final long serialVersionUID = 1L;

    public static final OverviewStyles INSTANCE = new OverviewStyles();

    public OverviewStyles() {
        super(OverviewStyles.class, "OverviewStyles.css");
    }
}
