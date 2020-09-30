/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.models;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;

/**
 * @author mpostelnicu Converter for {@link GenericBootstrapFormComponent}
 *         viewModeField This will be used when
 *         {@link WebConstants#PARAM_PRINT} is true in the browser and will
 *         convert the model object to something printable (string-like)
 */
public class ViewModeConverterModel<T> implements IComponentAssignedModel<String> {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

    private final IModel<T> originalModel;

    public ViewModeConverterModel(final IModel<T> originalModel) {
        this.originalModel = originalModel;
    }

    @Override
    public IWrapModel<String> wrapOnAssignment(Component component) {
        return new AssignmentWrapper(component);
    }

    private class AssignmentWrapper extends LoadableDetachableModel<String> implements IWrapModel<String> {

        private final Component component;

        public AssignmentWrapper(Component component) {
            this.component = component;
        }

        @Override
        public IModel<?> getWrappedModel() {
            return ViewModeConverterModel.this;
        }

        @Override
        protected String load() {
            final T object = originalModel.getObject();
            if (object == null) {
                return "";
            }

            // for booleans we return yes/no
            if (object instanceof Boolean) {
                String resourceKey = ((Boolean) object) ? "ViewModeConverterModel.yes" : "ViewModeConverterModel.no";
                return new StringResourceModel(resourceKey, component).getString();
            }

            // for collections that are empty, we return empty
            if (object instanceof Collection<?>) {
                if (((Collection<?>) object).size() == 0) {
                    return "";
                }
            }

            // convert date to a nicer format
            if (object instanceof Date) {
                return SDF.format((Date) object);
            }

            // alas just return the string value of the object
            return object.toString();
        }
    }

    @Override
    public String getObject() {
        throw new IllegalStateException("Must be assigned to a component");
    }
}
