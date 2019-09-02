package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
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
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.service.form.TenderItemService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class PurchaseItemPanel extends ListViewSectionPanel<PurchaseItem, PurchaseRequisition> {
    private GenericSleepFormComponent unit;

    private GenericSleepFormComponent totalCost;

    @SpringBean
    private TenderItemService tenderItemService;

    public PurchaseItemPanel(final String id) {
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
            final List<PurchaseItem> purchaseItems = PurchaseItemPanel.this.getModelObject();
            for (final PurchaseItem purchaseItem : purchaseItems) {
                if (purchaseItem.getPlanItem() != null) {
                    planItems.add(purchaseItem.getPlanItem());
                }
            }

            if (purchaseItems.size() != 0 && purchaseItems.size() != planItems.size()) {
                final ListView<PurchaseItem> list = (ListView<PurchaseItem>) PurchaseItemPanel.this
                        .get("listWrapper").get("list");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        final TransparentWebMarkupContainer accordion =
                                (TransparentWebMarkupContainer) list.get("" + i).get(ID_ACCORDION);

                        final GenericBootstrapFormComponent planItem =
                                (GenericBootstrapFormComponent) accordion.get(ID_ACCORDION_TOGGLE)
                                        .get("headerField").get("planItem");

                        if (planItem != null) {
                            planItem.getField().error(getString("uniqueItem"));

                            final Optional<AjaxRequestTarget> target = RequestCycle.get().find(AjaxRequestTarget.class);
                            if (target.isPresent()) {
                                target.get().add(planItem.getBorder());
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
    public PurchaseItem createNewChild(final IModel<PurchaseRequisition> parentModel) {
        final PurchaseItem child = new PurchaseItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    protected BootstrapDeleteButton getRemoveChildButton(final PurchaseItem item,
                                                         final NotificationPanel removeButtonNotificationPanel) {
        final BootstrapDeleteButton removeButton = new BootstrapDeleteButton("remove",
                new ResourceModel("removeButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                final List<TenderItem> tenderItems = tenderItemService.findByPurchaseItem(item);

                if (tenderItems.size() > 0) {
                    final ValidationError error = new ValidationError();
                    error.addKey("purchaseItemError");
                    error(error);
                } else {
                    PurchaseItemPanel.this.getModelObject().remove(item);
                    listView.removeAll();
                    target.add(listWrapper);
                }

                target.add(removeButtonNotificationPanel);
            }
        };

        removeButton.setOutputMarkupPlaceholderTag(true);
        return removeButton;
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

        unit = new GenericSleepFormComponent<>("unit",
                (IModel<Unit>) () -> {
                    if (item.getModelObject().getPlanItem() != null
                            && item.getModelObject().getPlanItem().getUnitOfIssue() != null) {
                        return item.getModelObject().getPlanItem().getUnitOfIssue();
                    }
                    return null;
                });
        unit.setOutputMarkupId(true);
        item.add(unit);

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
                (IModel<String>) () -> {
                    if (quantity.getModelObject() != null && amount.getModelObject() != null) {
                        return ComponentUtil.formatNumber(amount.getModelObject().multiply(quantity.getModelObject()));
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

            final Select2ChoiceBootstrapFormComponent<PlanItem> planItem =
                    new Select2ChoiceBootstrapFormComponent<PlanItem>("planItem",
                            new GenericChoiceProvider<>(planItems)) {
                        @Override
                        protected void onUpdate(final AjaxRequestTarget target) {
                            target.add(unit);
                        }
                    };
            planItem.required();
            planItem.add(new StopEventPropagationBehavior());

            final Component description = ComponentUtil.addTextField(this, "description");
            description.add(new StopEventPropagationBehavior());

            add(planItem);
        }
    }
}
