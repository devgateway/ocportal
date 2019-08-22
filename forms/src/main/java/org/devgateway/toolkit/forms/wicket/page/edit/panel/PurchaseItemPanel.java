package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class PurchaseItemPanel extends ListViewSectionPanel<PurchaseItem, PurchaseRequisition> {
    private GenericSleepFormComponent totalCost;

    public PurchaseItemPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public PurchaseItem createNewChild(final IModel<PurchaseRequisition> parentModel) {
        final PurchaseItem child = new PurchaseItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PurchaseItem> item) {
        final TextFieldBootstrapFormComponent<BigDecimal> quantity =
                new TextFieldBootstrapFormComponent<BigDecimal>("quantity") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        quantity.decimal();
        quantity.getField().add(RangeValidator.minimum(BigDecimal.ZERO));
        quantity.required();
        item.add(quantity);

        ComponentUtil.addTextField(item, "unit").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);


        final TextFieldBootstrapFormComponent<BigDecimal> amount =
                new TextFieldBootstrapFormComponent<BigDecimal>("amount") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        amount.decimal();
        amount.getField().add(RangeValidator.minimum(BigDecimal.ZERO));
        amount.required();
        item.add(amount);

        totalCost = new GenericSleepFormComponent<>("totalCost",
                (IModel<BigDecimal>) () -> {
                    if (quantity.getModelObject() != null && amount.getModelObject() != null) {
                        return amount.getModelObject().multiply(quantity.getModelObject());
                    }
                    return null;
                });
        totalCost.setOutputMarkupId(true);
        item.add(totalCost);
    }

    @Override
    protected boolean filterListItem(final PurchaseItem purchaseItem) {
        return true;
    }

    @Override
    protected Component getHeaderField(final String id, final CompoundPropertyModel<PurchaseItem> compoundModel) {
        return new PurchaseItemHeaderPanel(id, compoundModel);
    }

    private class PurchaseItemHeaderPanel extends GenericPanel<PurchaseItem> {
        PurchaseItemHeaderPanel(final String componentId, final IModel<PurchaseItem> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            // filtered the list based on form Procurement Plan
            final PurchaseRequisition parentObject =
                    (PurchaseRequisition) PurchaseItemPanel.this.getParent().getDefaultModelObject();
            final List<PlanItem> planItems = parentObject.getProject().getProcurementPlan().getPlanItems();

            final Select2ChoiceBootstrapFormComponent<PlanItem> planItem = new Select2ChoiceBootstrapFormComponent<>(
                    "planItem", new GenericChoiceProvider<>(planItems));
            planItem.required();
            planItem.add(new StopEventPropagationBehavior());

            final Component description = ComponentUtil.addTextField(this, "description");
            description.add(new StopEventPropagationBehavior());

            add(planItem);
        }
    }
}
