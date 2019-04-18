package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.ItemDetail;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PlanItemService;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class ItemDetailPanel extends ListViewSectionPanel<ItemDetail, PurchaseRequisition> {
    @SpringBean
    private PlanItemService planItemService;

    public ItemDetailPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public ItemDetail createNewChild(final IModel<PurchaseRequisition> parentModel) {
        final ItemDetail child = new ItemDetail();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<ItemDetail> item) {
        final Component planItem = ComponentUtil.addSelect2ChoiceField(item, "planItem", planItemService).required();
        planItem.add(new StopEventPropagationBehavior());

        ComponentUtil.addIntegerTextField(item, "quantity").required();
        ComponentUtil.addTextField(item, "unit").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        ComponentUtil.addDoubleField(item, "amount");
    }

    @Override
    protected boolean filterListItem(final ItemDetail itemDetail) {
        return true;
    }
}
