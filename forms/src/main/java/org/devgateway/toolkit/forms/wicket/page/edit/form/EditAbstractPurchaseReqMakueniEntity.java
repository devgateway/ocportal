package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
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

        // TODO - if we don't have a purchaseRequisition log and redirect to status page.
    }
}
