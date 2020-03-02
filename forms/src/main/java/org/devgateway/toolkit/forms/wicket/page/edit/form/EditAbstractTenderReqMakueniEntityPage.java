package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mihai
 */
public abstract class EditAbstractTenderReqMakueniEntityPage<T extends AbstractTenderProcessMakueniEntity>
        extends EditAbstractTenderProcessMakueniEntityPage<T> {
    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractTenderReqMakueniEntityPage.class);

    protected GenericSleepFormComponent<String> tenderTitle;

    protected GenericSleepFormComponent<String> tenderNumber;

    public EditAbstractTenderReqMakueniEntityPage(final PageParameters parameters) {
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

}
