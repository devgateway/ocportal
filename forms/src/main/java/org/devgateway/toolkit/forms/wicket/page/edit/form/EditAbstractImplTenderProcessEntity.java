package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;

/**
 * @author mpostelnicu
 */
public abstract class EditAbstractImplTenderProcessEntity<T extends AbstractTenderProcessMakueniEntity>
        extends EditAbstractTenderProcessMakueniEntity<T> {

    public EditAbstractImplTenderProcessEntity(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        entityButtonsFragment = new Fragment("extraReadOnlyFields", "implExtraFields", this);
        entityButtonsFragment.add(new GenericSleepFormComponent<>("tenderProcess.tender.iterator.next.tenderTitle"));

        editForm.replace(entityButtonsFragment);
    }
}
