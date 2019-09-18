package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TenderItemPanel extends ListViewSectionPanel<TenderItem, Tender> {
    private GenericSleepFormComponent unit;

    private GenericSleepFormComponent totalCost;

    public TenderItemPanel(final String id) {
        super(id);
    }

    protected class ListItemsValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            final Set<PlanItem> planItems = new HashSet<>();
            final List<TenderItem> tenderItems = TenderItemPanel.this.getModelObject();
            for (final TenderItem tenderItem : tenderItems) {
                if (tenderItem.getPurchaseItem() != null && tenderItem.getPurchaseItem().getPlanItem() != null) {
                    planItems.add(tenderItem.getPurchaseItem().getPlanItem());
                }
            }

            if (tenderItems.size() != 0 && tenderItems.size() != planItems.size()) {
                final ListView<TenderItem> list = (ListView<TenderItem>) TenderItemPanel.this
                        .get("listWrapper").get("list");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        final TransparentWebMarkupContainer accordion =
                                (TransparentWebMarkupContainer) list.get("" + i).get(ID_ACCORDION);

                        final GenericBootstrapFormComponent purchaseItem =
                                (GenericBootstrapFormComponent) accordion.get(ID_ACCORDION_TOGGLE)
                                        .get("headerField").get("purchaseItem");

                        if (purchaseItem != null) {
                            purchaseItem.getField().error(getString("uniqueItem"));

                            final Optional<AjaxRequestTarget> target = RequestCycle.get().find(AjaxRequestTarget.class);
                            if (target.isPresent()) {
                                target.get().add(purchaseItem.getBorder());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form form = (Form) getParent();
        if (form != null) {
            form.add(new ListItemsValidator());
        }
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
        quantity.getField().add(RangeValidator.minimum(BigDecimal.ONE), new BigDecimalValidator());
        quantity.required();
        item.add(quantity);

        unit = new GenericSleepFormComponent<>("unit",
                (IModel<Unit>) () -> {
                    if (item.getModelObject().getPurchaseItem() != null
                            && item.getModelObject().getPurchaseItem().getPlanItem() != null
                            && item.getModelObject().getPurchaseItem().getPlanItem().getUnitOfIssue() != null) {
                        return item.getModelObject().getPurchaseItem().getPlanItem().getUnitOfIssue();
                    }
                    return null;
                });
        unit.setOutputMarkupId(true);
        item.add(unit);


        final TextFieldBootstrapFormComponent<BigDecimal> price =
                new TextFieldBootstrapFormComponent<BigDecimal>("unitPrice") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        price.decimal();
        price.required();
        price.getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        item.add(price);

        totalCost = new GenericSleepFormComponent<>("totalCost",
                (IModel<String>) () -> {
                    if (quantity.getModelObject() != null && price.getModelObject() != null) {
                        return ComponentUtil.formatNumber(price.getModelObject().multiply(quantity.getModelObject()));
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
                    new Select2ChoiceBootstrapFormComponent<PurchaseItem>(
                            "purchaseItem", new GenericChoiceProvider<>(purchaseItems)) {
                        @Override
                        protected void onUpdate(final AjaxRequestTarget target) {
                            target.add(unit);
                        }
                    };
            purchaseItem.required();
            purchaseItem.add(new StopEventPropagationBehavior());

            final Component description = ComponentUtil.addTextField(this, "description");
            description.add(new StopEventPropagationBehavior());

            add(purchaseItem);
        }
    }
}
