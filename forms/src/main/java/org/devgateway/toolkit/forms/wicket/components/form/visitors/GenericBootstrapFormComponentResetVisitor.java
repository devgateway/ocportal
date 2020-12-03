package org.devgateway.toolkit.forms.wicket.components.form.visitors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.toolkit.forms.models.ResettableModel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;

/**
 * @author Nadejda Mandrescu
 */
public class GenericBootstrapFormComponentResetVisitor implements IVisitor<GenericBootstrapFormComponent<?, ?>, Void> {

    @Override
    public void component(final GenericBootstrapFormComponent<?, ?> object, final IVisit<Void> visit) {
        visit.dontGoDeeper();

        if (object.isEnabled()) {
            IModel<?> model = object.getModel();
            if (model instanceof ResettableModel) {
                ((ResettableModel) model).reset();
            }
        }
    }

}
