package org.devgateway.toolkit.forms.wicket.components.util;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author Octavian Ciubotaru
 */
public final class EditViewResourceModel {

    private EditViewResourceModel() {
    }

    public static IModel<String> of(boolean edit, String resourceKey, Component component) {
        String editViewResourceKey = edit ? "EditViewResourceModel.edit" : "EditViewResourceModel.view";
        return new StringResourceModel(editViewResourceKey, component)
                .setParameters(new StringResourceModel(resourceKey, component));
    }
}
