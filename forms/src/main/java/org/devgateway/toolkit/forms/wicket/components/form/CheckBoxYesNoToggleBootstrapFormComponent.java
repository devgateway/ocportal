package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstraptoggle.BootstrapToggle;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkbox.bootstraptoggle.BootstrapToggleConfig;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author idobre
 * @since 11/3/16
 */
public class CheckBoxYesNoToggleBootstrapFormComponent
        extends GenericEnablingBootstrapFormComponent<Boolean, BootstrapToggle> {
    private static final long serialVersionUID = -4032850928243673675L;

    private CheckBox wrappedCheckbox;
    private boolean removeCheckboxClass = false;

    public CheckBoxYesNoToggleBootstrapFormComponent(final String id, final IModel<String> labelModel,
                                                     final IModel<Boolean> model) {
        super(id, labelModel, model);
    }

    /**
     * @param id
     * @param model
     */
    public CheckBoxYesNoToggleBootstrapFormComponent(final String id, final IModel<Boolean> model) {
        super(id, model);
    }

    public CheckBoxYesNoToggleBootstrapFormComponent(final String id) {
        super(id);
    }

    public CheckBoxYesNoToggleBootstrapFormComponent(final String id, Boolean removeCheckboxClass) {
        super(id);
        this.removeCheckboxClass = removeCheckboxClass;
    }

    @Override
    protected FormComponent<Boolean> updatingBehaviorComponent() {
        return wrappedCheckbox;
    }

    @Override
    protected BootstrapToggle inputField(final String id, final IModel<Boolean> model) {
        final BootstrapToggleConfig config = new BootstrapToggleConfig();
        config.withOnStyle(BootstrapToggleConfig.Style.success)
                .withOffStyle(BootstrapToggleConfig.Style.danger)
                .withStyle("customCssClass");

        final BootstrapToggle checkBoxToggle = new BootstrapToggle("field", initFieldModel(), config) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> getOffLabel() {
                return new StringResourceModel("no", this);
            }

            @Override
            protected IModel<String> getOnLabel() {
                return new StringResourceModel("yes", this);
            }

            @Override
            protected CheckBox newCheckBox(final String id, final IModel<Boolean> model) {
                wrappedCheckbox = super.newCheckBox(id, model);
                wrappedCheckbox.add(new AjaxFormComponentUpdatingBehavior("change") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        CheckBoxYesNoToggleBootstrapFormComponent.this.onUpdate(target);
                    }
                });
                return wrappedCheckbox;
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();
                if (removeCheckboxClass) {
                    this.get("wrapper").add(new AttributeModifier("class", ""));
                }
            }
        };

        return checkBoxToggle;
    }

    @Override
    public String getUpdateEvent() {
        return "change";
    }

    @Override
    protected boolean boundComponentsVisibilityAllowed(final Boolean selectedValue) {
        return selectedValue;
    }

    public CheckBox getWrappedCheckbox() {
        return wrappedCheckbox;
    }
}

