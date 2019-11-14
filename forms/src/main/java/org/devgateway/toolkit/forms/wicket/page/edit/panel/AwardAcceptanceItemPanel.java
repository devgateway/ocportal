package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.SupplierResponse;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.service.category.SupplierResponseService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mpostelnicu
 */
public class AwardAcceptanceItemPanel extends ListViewSectionPanel<AwardAcceptanceItem, AwardAcceptance> {

    private GenericSleepFormComponent<String> supplierID;
    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    @SpringBean
    protected SupplierResponseService supplierResponseService;


    protected List<GenericBootstrapFormComponent<?, ?>> getChildComponentsByName(String name) {
        ArrayList<GenericBootstrapFormComponent<?, ?>> ret = new ArrayList<>();
        listView.forEach(c -> ret.add((GenericBootstrapFormComponent<?, ?>) c.get(name)));
        return ret;
    }

    protected List<GenericBootstrapFormComponent<?, ?>> getSupplierResponseComponents() {
        return getChildComponentsByName("supplierResponse");
    }

    protected FormComponent<?>[] getFormComponentsFromBootstrapComponents(List<GenericBootstrapFormComponent<?, ?>>
                                                                                  bc) {
        return bc.stream().map(GenericBootstrapFormComponent::getField).toArray(FormComponent[]::new);
    }

    protected boolean getWrongAcceptedCount() {
        return AwardAcceptanceItemPanel.this.getModelObject().stream()
                .map(AwardAcceptanceItem::getSupplierResponse)
                .filter(Objects::nonNull).map(SupplierResponse::getLabel)
                .filter(s -> s.equals(DBConstants.SupplierResponse.ACCEPTED)).count() > 1;
    }

    public AwardAcceptanceItemPanel(final String id) {
        super(id);
    }

    protected void addErrorAndRefreshComponents(List<GenericBootstrapFormComponent<?, ?>> components, String key) {
        components.stream().map(GenericBootstrapFormComponent::getField)
                .forEach(f -> f.error(new ValidationError(getString(key))));
        RequestCycle.get().find(AjaxRequestTarget.class).ifPresent(
                ajaxRequestTarget -> components.forEach(ajaxRequestTarget::add));
    }

    protected class ListItemsValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return getFormComponentsFromBootstrapComponents(getSupplierResponseComponents());
        }

        @Override
        public void validate(Form<?> form) {
            if (getWrongAcceptedCount()) {
                form.error(getString("oneAwardAccepted"));
                addErrorAndRefreshComponents(getSupplierResponseComponents(), "oneAwardAccepted");
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

        Select2ChoiceBootstrapFormComponent<SupplierResponse> supplierResponse = ComponentUtil.addSelect2ChoiceField(
                item, "supplierResponse", supplierResponseService);
        supplierResponse.required();

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
