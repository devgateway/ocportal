package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mihai
 */
public abstract class EditAbstractTenderReqMakueniEntity<T extends AbstractPurchaseReqMakueniEntity>
        extends EditAbstractPurchaseReqMakueniEntity<T> {
    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractTenderReqMakueniEntity.class);

    protected GenericSleepFormComponent<String> tenderTitle;

    protected GenericSleepFormComponent<String> tenderNumber;

    public EditAbstractTenderReqMakueniEntity(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addTenderInfo();
    }

    private void addTenderInfo() {
        tenderTitle = new GenericSleepFormComponent<>("tenderNumber", (IModel<String>) () ->
                editForm.getModelObject().getPurchaseRequisition().getTender().getTenderTitle()
        );
        editForm.add(tenderTitle);

        tenderNumber = new GenericSleepFormComponent<>("tenderTitle", (IModel<String>) () ->
                editForm.getModelObject().getPurchaseRequisition().getTender().getTenderNumber()
        );
        editForm.add(tenderNumber);
    }

}
