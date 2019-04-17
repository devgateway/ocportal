package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.persistence.dao.form.ItemDetail;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class ItemDetailPanel extends ListViewSectionPanel<ItemDetail, PurchaseRequisition> {
    public ItemDetailPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public ItemDetail createNewChild(IModel<PurchaseRequisition> parentModel) {
        final ItemDetail child = new ItemDetail();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(ListItem<ItemDetail> item) {

    }

    @Override
    protected boolean filterListItem(ItemDetail itemDetail) {
        return true;
    }
}
