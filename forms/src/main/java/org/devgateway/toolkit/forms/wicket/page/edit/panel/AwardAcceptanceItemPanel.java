package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.SupplierResponse;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.service.category.SupplierResponseService;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mpostelnicu
 */
public class AwardAcceptanceItemPanel extends ListViewSectionPanel<AwardAcceptanceItem, AwardAcceptance> {

    private GenericSleepFormComponent<String> supplierID;
    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    @SpringBean
    protected SupplierResponseService supplierResponseService;


    protected List<GenericBootstrapFormComponent<?, ?>> getSupplierResponseComponents() {
        return getChildComponentsByName("supplierResponse");
    }


    protected boolean getWrongAcceptedCount() {
        return AwardAcceptanceItemPanel.this.getModelObject()
                .stream()
                .filter(AwardAcceptanceItem::isAccepted)
                .count() > 1;
    }

    protected List<GenericBootstrapFormComponent<?, ?>> getAwardeeComponents() {
        return getChildComponentsByName("awardee");
    }

    protected boolean getWrongDistinctCount() {
        long distinctCount = AwardAcceptanceItemPanel.this.getModelObject()
                .stream()
                .map(AwardAcceptanceItem::getAwardee)
                .distinct()
                .count();

        return distinctCount != AwardAcceptanceItemPanel.this.getModelObject().size();
    }

    protected class WrongDistinctCountValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return ComponentUtil.getFormComponentsFromBootstrapComponents(getAwardeeComponents());
        }

        @Override
        public void validate(Form<?> form) {
            if (getWrongDistinctCount()) {
                form.error(getString("wrongDistinctCount"));
                addErrorAndRefreshComponents(getAwardeeComponents(), "wrongDistinctCount");
            }
        }
    }

    protected boolean getWrongAwardNotificationCount() {
        AwardAcceptance awardAcceptance = (AwardAcceptance) Form.findForm(AwardAcceptanceItemPanel.this)
                .getModelObject();
        AwardNotification awardNotification = awardAcceptance.getTenderProcess().getSingleAwardNotification();
        return awardNotification.getItems().size() != (long) AwardAcceptanceItemPanel.this.getModelObject().size();
    }


    public AwardAcceptanceItemPanel(final String id) {
        super(id);
    }

    protected class OneAwardAcceptedValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return ComponentUtil.getFormComponentsFromBootstrapComponents(getAwardeeComponents());
        }

        @Override
        public void validate(Form<?> form) {
            if (getWrongAcceptedCount()) {
                form.error(getString("oneAwardAccepted"));
                addErrorAndRefreshComponents(getSupplierResponseComponents(), "oneAwardAccepted");
            }
        }
    }

    protected class AwardNotificationCountValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            if (getWrongAwardNotificationCount()) {
                form.error(getString("wrongAwardNotificationCount"));
            }
        }
    }

    @Override
    protected BootstrapAddButton getAddNewChildButton() {
        return new AddNewChildButton("newButton", Model.of("New Award Acceptance"));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form form = findParent(Form.class);
        form.add(new OneAwardAcceptedValidator());
        form.add(new WrongDistinctCountValidator());
        form.add(new AwardNotificationCountValidator());
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
        TextFieldBootstrapFormComponent<BigDecimal> acceptedValue = ComponentUtil.addBigDecimalField(
                item, "acceptedAwardValue");
        acceptedValue.getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        acceptedValue.setOutputMarkupPlaceholderTag(true);

        ComponentUtil.addDateField(item, "acceptanceDate");

        final Select2ChoiceBootstrapFormComponent<SupplierResponse> supplierResponse =
                new Select2ChoiceBootstrapFormComponent<SupplierResponse>(
                        "supplierResponse",
                        new GenericPersistableJpaTextChoiceProvider<>(supplierResponseService)
                ) {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        acceptedValue.setVisibilityAllowed(item.getModelObject().isAccepted());
                        target.add(acceptedValue);
                    }
                };
        item.add(supplierResponse);

        acceptedValue.setVisibilityAllowed(item.getModelObject().isAccepted());

        addSupplierInfo(item);

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        item.add(formDocs);
    }

    private void addSupplierInfo(ListItem<AwardAcceptanceItem> item) {
        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>(
                "awardee",
                new GenericChoiceProvider<>(ComponentUtil.getSuppliersInTenderQuotation(
                        item.getModelObject().getParent().getTenderProcess(), true))
        );
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
