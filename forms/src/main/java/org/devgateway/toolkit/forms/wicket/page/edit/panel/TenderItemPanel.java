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
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;

import java.math.BigDecimal;
import java.util.List;

public class TenderItemPanel extends ListViewSectionPanel<TenderItem, Tender> {
    private GenericSleepFormComponent totalCost;

    public TenderItemPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public TenderItem createNewChild(final IModel<Tender> parentModel) {
        final TenderItem child = new TenderItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<TenderItem> item) {
        final TextFieldBootstrapFormComponent<BigDecimal> quantity =
                new TextFieldBootstrapFormComponent<BigDecimal>("quantity") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        quantity.decimal();
        quantity.getField().add(RangeValidator.minimum(1));
        quantity.required();
        item.add(quantity);

        ComponentUtil.addTextField(item, "unitOfIssue").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);


        final TextFieldBootstrapFormComponent<BigDecimal> price =
                new TextFieldBootstrapFormComponent<BigDecimal>("unitPrice") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        price.decimal();
        price.required();
        price.getField().add(RangeValidator.minimum(BigDecimal.ZERO));
        item.add(price);

        totalCost = new GenericSleepFormComponent<>("totalCost",
                (IModel<BigDecimal>) () -> {
                    if (quantity.getModelObject() != null && price.getModelObject() != null) {
                        return price.getModelObject().multiply(quantity.getModelObject());
                    }
                    return null;
                });
        totalCost.setOutputMarkupId(true);
        item.add(totalCost);
    }

    @Override
    protected boolean filterListItem(final TenderItem tenderItem) {
        return true;
    }

    @Override
    protected Component getHeaderField(final String id, final CompoundPropertyModel<TenderItem> compoundModel) {
        return new TenderItemHeaderPanel(id, compoundModel);
    }

    private class TenderItemHeaderPanel extends GenericPanel<TenderItem> {
        TenderItemHeaderPanel(final String componentId, final IModel<TenderItem> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            // filtered the list based on form Purchase Requisition
            final Tender parentObject = (Tender) TenderItemPanel.this.getParent().getDefaultModelObject();
            final List<PurchaseItem> purchaseItems = parentObject.getPurchaseRequisition().getPurchaseItems();

            final Select2ChoiceBootstrapFormComponent<PurchaseItem> purchaseItem =
                    new Select2ChoiceBootstrapFormComponent<>(
                            "purchaseItem", new GenericChoiceProvider<>(purchaseItems));
            purchaseItem.required();
            purchaseItem.add(new StopEventPropagationBehavior());

            add(purchaseItem);
        }
    }
}
