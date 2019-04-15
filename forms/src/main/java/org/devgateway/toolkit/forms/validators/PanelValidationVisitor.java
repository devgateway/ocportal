package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;

/**
 * @author idobre
 * @since 2019-04-15
 *
 * Validator used in {@link ListViewSectionPanel} add new element validation.
 */
public class PanelValidationVisitor implements IVisitor<GenericBootstrapFormComponent<?, ?>, Void> {
    private Boolean formErrors = false;

    private AjaxRequestTarget target;

    public PanelValidationVisitor(final AjaxRequestTarget target) {
        this.target = target;
    }

    @Override
    public void component(final GenericBootstrapFormComponent<?, ?> object, final IVisit<Void> visit) {
        visit.dontGoDeeper();

        // no need to use the processInput() function here, we only want to read the new input and validate it.
        object.getField().inputChanged();
        object.getField().validate();

        if (object.getField().isValid()) {
            return;
        }

        formErrors = true;
        target.add(object.getBorder());
    }

    public Boolean getFormErrors() {
        return formErrors;
    }
}
