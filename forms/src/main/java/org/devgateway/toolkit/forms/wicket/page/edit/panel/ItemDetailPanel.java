package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
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

    private GenericSleepFormComponent totalCost;

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
        final TextFieldBootstrapFormComponent<Integer> quantity =
                new TextFieldBootstrapFormComponent<Integer>("quantity") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        quantity.integer();
        quantity.required();
        item.add(quantity);

        ComponentUtil.addTextField(item, "unit").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);


        final TextFieldBootstrapFormComponent<Double> amount =
                new TextFieldBootstrapFormComponent<Double>("amount") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        amount.asDouble();
        amount.required();
        item.add(amount);

        totalCost = new GenericSleepFormComponent<>("totalCost",
                (IModel<Double>) () -> {
                    if (quantity.getModelObject() != null && amount.getModelObject() != null) {
                        return quantity.getModelObject() * amount.getModelObject();
                    }
                    return null;
                });
        totalCost.setOutputMarkupId(true);
        item.add(totalCost);
    }

    @Override
    protected boolean filterListItem(final ItemDetail itemDetail) {
        return true;
    }

    @Override
    protected Component getHeaderField(final String id, final CompoundPropertyModel<ItemDetail> compoundModel) {
        return new ItemDetailHeaderPanel(id, compoundModel);
    }

    private class ItemDetailHeaderPanel extends GenericPanel<ItemDetail> {
        ItemDetailHeaderPanel(final String componentId, final IModel<ItemDetail> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final Component planItem = ComponentUtil.addSelect2ChoiceField(this, "planItem", planItemService)
                    .required();
            planItem.add(new StopEventPropagationBehavior());
        }
    }
}
