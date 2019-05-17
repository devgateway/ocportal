package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mihai
 */
public abstract class EditAbstractPurchaseReqMakueniEntity<T extends AbstractPurchaseReqMakueniEntity>
        extends EditAbstractMakueniEntityPage<T> {

    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractPurchaseReqMakueniEntity.class);
    protected final PurchaseRequisition purchaseRequisition;

    public EditAbstractPurchaseReqMakueniEntity(final PageParameters parameters) {
        super(parameters);


        this.purchaseRequisition = SessionUtil.getSessionPurchaseRequisition();
    }

}
