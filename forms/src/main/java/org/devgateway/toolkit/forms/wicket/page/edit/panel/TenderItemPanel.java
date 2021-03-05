package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
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
import org.devgateway.toolkit.forms.wicket.events.AjaxUpdateEvent;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TenderItemPanel extends ListViewSectionPanel<TenderItem, Tender> {

    public TenderItemPanel(final String id) {
        super(id);
    }

    protected class TenderListItemsValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            final Set<PlanItem> planItems = new HashSet<>();
            final List<TenderItem> tenderItems = TenderItemPanel.this.getModelObject();

            for (final TenderItem tenderItem : tenderItems) {
                if (tenderItem.getPlanItem() != null && tenderItem.getPurchaseItem() != null) {
                    form.error(getString("purchaseItemPlanItemNotAllowed"));
                }
                if (tenderItem.getPurchaseItem() != null && tenderItem.getPurchaseItem().getPlanItem() != null) {
                    planItems.add(tenderItem.getPurchaseItem().getPlanItem());
                } else {
                    if (tenderItem.getPlanItem() != null) {
                        planItems.add(tenderItem.getPlanItem());
                    }
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

                        final GenericBootstrapFormComponent planItem =
                                (GenericBootstrapFormComponent) accordion.get(ID_ACCORDION_TOGGLE)
                                        .get("headerField").get("planItem");

                        if (purchaseItem != null) {
                            reportItemError(purchaseItem);
                        } else {
                            if (planItem != null) {
                                reportItemError(planItem);
                            }
                        }
                    }
                }
            }
        }
    }

    public void reportItemError(GenericBootstrapFormComponent<?, ?> c) {
            c.getField().error(getString("uniqueItem"));
            RequestCycle.get().find(AjaxRequestTarget.class).ifPresent(ajaxRequestTarget ->
                    ajaxRequestTarget.add(c.getBorder()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        findParent(Form.class).add(new TenderListItemsValidator());
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
        GenericSleepFormComponent<Unit> unit;
        GenericSleepFormComponent<String> totalCost;
        final TextFieldBootstrapFormComponent<BigDecimal> quantity =
                new TextFieldBootstrapFormComponent<BigDecimal>("quantity") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        super.onUpdate(target);
                        send(this.getParent(), Broadcast.BREADTH, new AjaxUpdateEvent(target, "totalCost"));

                    }
                };
        quantity.decimal();
        quantity.getField().add(RangeValidator.minimum(BigDecimal.ONE), new BigDecimalValidator());
        //quantity.required();
        item.add(quantity);

        unit = new GenericSleepFormComponent<Unit>("unit",
                (IModel<Unit>) () -> {
                    if (item.getModelObject().getPurchaseItem() != null
                            && item.getModelObject().getPurchaseItem().getPlanItem() != null
                            && item.getModelObject().getPurchaseItem().getPlanItem().getUnitOfIssue() != null) {
                        return item.getModelObject().getPurchaseItem().getPlanItem().getUnitOfIssue();
                    } else {
                        if (item.getModelObject().getPlanItem() != null
                                && item.getModelObject().getPlanItem() != null
                                && item.getModelObject().getPlanItem().getUnitOfIssue() != null) {
                            return item.getModelObject().getPlanItem().getUnitOfIssue();
                        }
                    }
                    return null;
                }) {
            @Override
            public void onEvent(IEvent<?> event) {
                AjaxUpdateEvent.refreshIfPayloadMatches(event, this, "unit");
            }
        };
        unit.setOutputMarkupId(true);
        item.add(unit);


        final TextFieldBootstrapFormComponent<BigDecimal> price =
                new TextFieldBootstrapFormComponent<BigDecimal>("unitPrice") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        send(this.getParent(), Broadcast.BREADTH, new AjaxUpdateEvent(target, "totalCost"));
                    }
                };
        price.decimal();
        //price.required();
        price.getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        item.add(price);

        totalCost = new GenericSleepFormComponent<String>("totalCost",
                (IModel<String>) () -> {
                    if (quantity.getModelObject() != null && price.getModelObject() != null) {
                        return ComponentUtil.formatNumber(price.getModelObject().multiply(quantity.getModelObject()));
                    }
                    return null;
                }) {
            @Override
            public void onEvent(IEvent<?> event) {
                AjaxUpdateEvent.refreshIfPayloadMatches(event, this, "totalCost");
            }
        };
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

            final Select2ChoiceBootstrapFormComponent<PurchaseItem> purchaseItem =
                    new Select2ChoiceBootstrapFormComponent<PurchaseItem>(
                            "purchaseItem", new GenericChoiceProvider<>(
                            parentObject.getTenderProcess()
                                    .getPurchaseRequisition().stream().flatMap(pr -> pr.getPurchaseItems().stream())
                                    .collect(Collectors.toList())
                    )) {
                        @Override
                        protected void onUpdate(final AjaxRequestTarget target) {
                            super.onUpdate(target);
                            send(ComponentUtil.findFirstParentById(this.getParent(), ID_ACCORDION).getParent(),
                                    Broadcast.BREADTH, new AjaxUpdateEvent(target, "unit"));
                        }
                    };
            //purchaseItem.required();
            purchaseItem.add(new StopEventPropagationBehavior());


            final Select2ChoiceBootstrapFormComponent<PlanItem> planItem =
                    new Select2ChoiceBootstrapFormComponent<PlanItem>(
                            "planItem", new GenericChoiceProvider<>(
                            parentObject.getTenderProcess().getProcurementPlan().getPlanItems())) {
                        @Override
                        protected void onUpdate(final AjaxRequestTarget target) {
                            super.onUpdate(target);
                            send(ComponentUtil.findFirstParentById(this.getParent(), ID_ACCORDION).getParent(),
                                    Broadcast.BREADTH, new AjaxUpdateEvent(target, "unit"));
                        }
                    };
            //purchaseItem.required();
            planItem.add(new StopEventPropagationBehavior());

            add(planItem);

            final Component description = ComponentUtil.addTextField(this, "description");
            description.add(new StopEventPropagationBehavior());

            add(purchaseItem);
        }
    }
}
