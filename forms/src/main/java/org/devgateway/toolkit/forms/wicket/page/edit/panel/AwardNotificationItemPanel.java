package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.AfterThanDateValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Tender;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mpostelnicu
 */
public class AwardNotificationItemPanel extends ListViewSectionPanel<AwardNotificationItem, AwardNotification> {

    public AwardNotificationItemPanel(final String id) {
        super(id);
    }

    protected List<GenericBootstrapFormComponent<?, ?>> getAwardeeComponents() {
        return getChildComponentsByName("awardee");
    }

    protected boolean getWrongProfessionalOpinionCount() {
        AwardNotification awardNotification = (AwardNotification) Form.findForm(AwardNotificationItemPanel.this)
                .getModelObject();
        ProfessionalOpinion professionalOpinion = awardNotification.getTenderProcess()
                .getSingleProfessionalOpinion();


        return professionalOpinion.getItems().size() != AwardNotificationItemPanel.this.getModelObject().size();
    }

    protected boolean getWrongDistinctCount() {
        long distinctCount = AwardNotificationItemPanel.this.getModelObject()
                .stream()
                .map(AwardNotificationItem::getAwardee)
                .distinct()
                .count();

        return distinctCount != AwardNotificationItemPanel.this.getModelObject().size();
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


    @Override
    protected BootstrapAddButton getAddNewChildButton() {
        return new AddNewChildButton("newButton", new StringResourceModel("newAwardNotification", this));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        final Form<?> form = findParent(Form.class);
        form.add(new  WrongDistinctCountValidator());
    }

    @Override
    public AwardNotificationItem createNewChild(final IModel<AwardNotification> parentModel) {
        final AwardNotificationItem child = new AwardNotificationItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<AwardNotificationItem> item) {
        ComponentUtil.addBigDecimalField(item, "awardValue")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());

        final DateFieldBootstrapFormComponent awardDate = ComponentUtil.addDateField(item, "awardDate");
        final Tender tender = item.getModelObject().getParent().getTenderProcess().getSingleTender();
        if (tender != null && tender.getInvitationDate() != null) {
            awardDate.getField().add(new AfterThanDateValidator(tender.getInvitationDate()));
        }

        ComponentUtil.addDateField(item, "tenderAwardDate")
                .setShowTooltip(true);

        TextFieldBootstrapFormComponent<Integer> acknowledgementDays =
                ComponentUtil.addIntegerTextField(item, "acknowledgementDays");
        acknowledgementDays.getField().add(RangeValidator.minimum(0));
        acknowledgementDays.setShowTooltip(true);

        addSupplierInfo(item);

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        item.add(formDocs);

        final FileInputBootstrapFormComponent lettersOfRegret = new FileInputBootstrapFormComponent("lettersOfRegret");
        item.add(lettersOfRegret);
    }

    private void addSupplierInfo(ListItem<AwardNotificationItem> item) {
        GenericSleepFormComponent<String> supplierID = new GenericSleepFormComponent<>("supplierID",
                item.getModel().map(AwardNotificationItem::getAwardee)
                        .map(Supplier::getCode));
        supplierID.setOutputMarkupId(true);
        item.add(supplierID);

        GenericSleepFormComponent<String> supplierAddress = new GenericSleepFormComponent<>("supplierAddress",
                item.getModel().map(AwardNotificationItem::getAwardee)
                        .map(Supplier::getAddress));
        supplierAddress.setOutputMarkupId(true);
        item.add(supplierAddress);

        Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector
                = new Select2ChoiceBootstrapFormComponent<Supplier>(
                "awardee",
                new GenericChoiceProvider<>(
                        ComponentUtil.getSuppliersInTenderQuotation(
                                item.getModelObject().getParent().getTenderProcess(), true))
        ) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(supplierID, supplierAddress);
            }
        };
        item.add(awardeeSelector);

    }
}
