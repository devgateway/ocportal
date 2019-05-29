package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class EditAbstractMakueniEntityPage<T extends AbstractMakueniEntity>
        extends AbstractEditStatusEntityPage<T> {

    @SpringBean
    protected SessionMetadataService sessionMetadataService;

    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractMakueniEntityPage.class);

    public EditAbstractMakueniEntityPage(final PageParameters parameters) {
        super(parameters);

        this.listPageClass = DepartmentOverviewPage.class;
    }

    @Override
    protected void onAfterRevertToDraft(AjaxRequestTarget target) {
        editForm.getModelObject().getAllChildrenInHierarchy();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        enableDisableAutosaveFields(null);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
                BaseStyles.class, "assets/js/formLeavingHelper.js")));
    }
}
