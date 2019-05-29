package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
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

    protected final LoadableDetachableModel<PurchaseRequisition> purchaseRequisitionModel;

    @Override
    protected void checkAndSendEventForDisableEditing() {
        super.checkAndSendEventForDisableEditing();
        if (isTerminated()) {
            send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());
        }
    }


    protected PurchaseRequisition getPurchaseRequisition() {
        return purchaseRequisitionModel.getObject();
    }

    @Override
    public boolean isTerminated() {
        return super.isTerminated() || (getPurchaseRequisition() != null && getPurchaseRequisition().isTerminated());
    }


    public EditAbstractPurchaseReqMakueniEntity(final PageParameters parameters) {
        super(parameters);

        purchaseRequisitionModel = new LoadableDetachableModel<PurchaseRequisition>() {
            @Override
            protected PurchaseRequisition load() {
                return sessionMetadataService.getSessionPurchaseRequisition();
            }
        };

        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && getPurchaseRequisition() == null) {
            logger.warn("Something wrong happened since we are trying to add a new AbstractPurchaseReqMakueni Entity "
                    + "without having a PurchaseRequisition!");
            setResponsePage(StatusOverviewPage.class);
        }
    }
}
