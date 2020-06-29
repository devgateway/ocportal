package org.devgateway.toolkit.forms.wicket.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.AbstractDateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.AbstractDateTextFieldConfig;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 */
public abstract class AbstractDateFieldBootstrapFormComponent<T>
        extends GenericBootstrapFormComponent<T, TextField<T>> {
    private static final long serialVersionUID = 6829640010904041758L;

    public static final String DEFAULT_FORMAT = "dd/MM/yy";

    /**
     * @param id
     * @param labelModel
     * @param model
     */
    public AbstractDateFieldBootstrapFormComponent(final String id, final IModel<String> labelModel,
            final IModel<T> model) {
        super(id, labelModel, model);
    }

    public AbstractDateFieldBootstrapFormComponent(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param model
     */
    public AbstractDateFieldBootstrapFormComponent(final String id, final IModel<T> model) {
        super(id, model);
    }

    @Override
    protected AbstractDateTextField inputField(final String id, final IModel<T> model) {
        AbstractDateTextFieldConfig config = newDateConfig()
                .withView(AbstractDateTextFieldConfig.View.Year)
                .withFormat(DEFAULT_FORMAT)
                .autoClose(true).calendarWeeks(true).forceParse(false).highlightToday(true).clearButton(true)
                .allowKeyboardNavigation(true)
                .showTodayButton(AbstractDateTextFieldConfig.TodayButton.LINKED)
                .withView(AbstractDateTextFieldConfig.View.Decade);

        return newDateTextField(id, config);
    }

    protected abstract AbstractDateTextFieldConfig newDateConfig();

    protected abstract AbstractDateTextField newDateTextField(String id, AbstractDateTextFieldConfig config);

    @Override
    public String getUpdateEvent() {
        return "change";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.devgateway.toolkit.forms.wicket.components.form.
     * GenericBootstrapFormComponent#onConfigure()
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        IndicatingAjaxLink<String> clearDateLink = new IndicatingAjaxLink<String>("clearDate") {
            private static final long serialVersionUID = -1705495886974891511L;

            @Override
            public void onClick(final AjaxRequestTarget target) {
                AbstractDateFieldBootstrapFormComponent.this.field.setModelObject(null);
                target.add(AbstractDateFieldBootstrapFormComponent.this.field);
            }
        };
        border.add(clearDateLink);
    }
}
