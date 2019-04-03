package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniForm;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class EditAbstractMakueniFormPage<T extends AbstractMakueniForm>
        extends AbstractEditStatusEntityPage<T> {
    public EditAbstractMakueniFormPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
                BaseStyles.class, "assets/js/formLeavingHelper.js")));
    }
}
