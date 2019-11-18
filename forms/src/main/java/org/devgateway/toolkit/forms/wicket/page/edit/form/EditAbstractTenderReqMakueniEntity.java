package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mihai
 */
public abstract class EditAbstractTenderReqMakueniEntity<T extends AbstractTenderProcessMakueniEntity>
        extends EditAbstractTenderProcessMakueniEntity<T> {
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
                PersistenceUtil.getNext(editForm.getModelObject().getTenderProcess().getTender())
                        .getTenderNumber());
        editForm.add(tenderTitle);

        tenderNumber = new GenericSleepFormComponent<>("tenderTitle", (IModel<String>) () ->
                PersistenceUtil.getNext(editForm.getModelObject().getTenderProcess().getTender())
                        .getTenderTitle());
        editForm.add(tenderNumber);
    }

    protected List<Supplier> getSuppliersInTenderQuotation() {
        final TenderProcess tenderProcess = editForm.getModelObject().getTenderProcess();
        final TenderQuotationEvaluation tenderQuotationEvaluation = PersistenceUtil.getNext(
                tenderProcess.getTenderQuotationEvaluation());
        List<Supplier> suppliers = new ArrayList<>();
        if (tenderQuotationEvaluation != null && tenderQuotationEvaluation.getBids() != null) {
            for (Bid bid : tenderQuotationEvaluation.getBids()) {
                if (DBConstants.SupplierResponsiveness.PASS.equalsIgnoreCase(bid.getSupplierResponsiveness())) {
                    suppliers.add(bid.getSupplier());
                }
            }
        }

        return suppliers;
    }

}
