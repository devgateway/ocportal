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
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.service.form.PurchaseItemService;

public class TenderItemPanel extends ListViewSectionPanel<TenderItem, Tender> {
    @SpringBean
    private PurchaseItemService purchaseItemService;

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

        ComponentUtil.addTextField(item, "unitOfIssue").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);


        final TextFieldBootstrapFormComponent<Double> price =
                new TextFieldBootstrapFormComponent<Double>("unitPrice") {
                    @Override
                    protected void onUpdate(final AjaxRequestTarget target) {
                        target.add(totalCost);
                    }
                };
        price.asDouble();
        price.required();
        item.add(price);

        totalCost = new GenericSleepFormComponent<>("totalCost",
                (IModel<Double>) () -> {
                    if (quantity.getModelObject() != null && price.getModelObject() != null) {
                        return quantity.getModelObject() * price.getModelObject();
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

            // TODO - this should be filtered based on form Procurement Plan
            final Component planItem = ComponentUtil.addSelect2ChoiceField(this, "purchaseItem", purchaseItemService)
                    .required();
            planItem.add(new StopEventPropagationBehavior());
        }
    }
}
