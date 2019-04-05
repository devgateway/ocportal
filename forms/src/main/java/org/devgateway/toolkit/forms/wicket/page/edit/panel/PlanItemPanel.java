package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;

/**
 * @author idobre
 * @since 2019-04-05
 */
public class PlanItemPanel extends ListViewSectionPanel<PlanItem, ProcurementPlan> {
    public PlanItemPanel(final String id) {
        super(id);
    }

    @Override
    public PlanItem createNewChild(IModel<ProcurementPlan> parentModel) {
        final PlanItem child = new PlanItem();
        child.setParent(parentModel.getObject());

        return child;
    }

    @Override
    public void populateCompoundListItem(ListItem<PlanItem> item) {

    }

    @Override
    protected boolean filterListItem(PlanItem planItem) {
        return false;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
