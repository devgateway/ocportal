package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;

import java.math.BigDecimal;

/**
 * @author mpostelnicu
 */
public class AwardAcceptanceItemPanel extends ListViewSectionPanel<AwardAcceptanceItem, AwardAcceptance> {

    private GenericSleepFormComponent<String> supplierID;
    private GenericSleepFormComponent<String> supplierAddress;
    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    public AwardAcceptanceItemPanel(final String id) {
        super(id);
    }

    protected class ListItemsValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
//            final Set<PlanItem> planItems = new HashSet<>();
//            final List<PurchaseItem> purchaseItems = PRequisitionPanel.this.getModelObject();
//            for (final PurchaseItem purchaseItem : purchaseItems) {
//                if (purchaseItem.getPlanItem() != null) {
//                    planItems.add(purchaseItem.getPlanItem());
//                }
//            }
//
//            if (purchaseItems.size() != 0 && purchaseItems.size() != planItems.size()) {
//                final ListView<PurchaseItem> list = (ListView<PurchaseItem>) PRequisitionPanel.this
//                        .get("listWrapper").get("list");
//                if (list != null) {
//                    for (int i = 0; i < list.size(); i++) {
//                        final TransparentWebMarkupContainer accordion =
//                                (TransparentWebMarkupContainer) list.get("" + i).get(ID_ACCORDION);
//
//                        final GenericBootstrapFormComponent planItem =
//                                (GenericBootstrapFormComponent) accordion.get(ID_ACCORDION_TOGGLE)
//                                        .get("headerField").get("planItem");
//
//                        if (planItem != null) {
//                            planItem.getField().error(getString("uniqueItem"));
//
//                          final Optional<AjaxRequestTarget> target = RequestCycle.get().find(AjaxRequestTarget.class);
//                            if (target.isPresent()) {
//                                target.get().add(planItem.getBorder());
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    @Override
    protected BootstrapAddButton getAddNewChildButton() {
        return new AddNewChildButton("newButton", Model.of("New Award Acceptance"));
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
    public AwardAcceptanceItem createNewChild(final IModel<AwardAcceptance> parentModel) {
        final AwardAcceptanceItem child = new AwardAcceptanceItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    class AwardeeAjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        AwardeeAjaxComponentUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(supplierID);
        }
    }

    @Override
    public void populateCompoundListItem(final ListItem<AwardAcceptanceItem> item) {
        ComponentUtil.addBigDecimalField(item, "acceptedAwardValue").required()
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        ComponentUtil.addDateField(item, "acceptanceDate").required();

        addSupplierInfo(item);

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        item.add(formDocs);
    }

    private void addSupplierInfo(ListItem<AwardAcceptanceItem> item) {
        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>(
                "awardee",
                new GenericChoiceProvider<>(ComponentUtil.getSuppliersInTenderQuotation(
                        item.getModelObject().getParent().getTenderProcess(), true))
        );
        awardeeSelector.required();
        awardeeSelector.getField().add(new AwardAcceptanceItemPanel.AwardeeAjaxComponentUpdatingBehavior("change"));
        item.add(awardeeSelector);

        supplierID = new GenericSleepFormComponent<>("supplierID", (IModel<String>) () -> {
            if (awardeeSelector.getModelObject() != null) {
                return awardeeSelector.getModelObject().getCode();
            }
            return null;
        });
        supplierID.setOutputMarkupId(true);
        item.add(supplierID);

    }


    @Override
    protected boolean filterListItem(final AwardAcceptanceItem purchaseItem) {
        return true;
    }


}
