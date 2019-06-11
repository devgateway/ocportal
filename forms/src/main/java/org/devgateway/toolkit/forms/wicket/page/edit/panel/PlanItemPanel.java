package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.feedback.FeedbackMessages;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.validators.PanelValidationVisitor;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-05
 */
public class PlanItemPanel extends ListViewSectionPanel<PlanItem, ProcurementPlan> {
    @SpringBean
    private ItemService itemService;

    @SpringBean
    private ProcurementMethodService procurementMethodService;

    @SpringBean
    private TargetGroupService targetGroupService;

    private final PlanItemFilterBean listFilterBean;

    public PlanItemPanel(final String id) {
        super(id);

        listFilterBean = new PlanItemFilterBean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    protected void addFilterPanel() {
        final PlanItemFilterPanel listFilterPanel = new PlanItemFilterPanel(
                "listFilterPanel", new CompoundPropertyModel<>(listFilterBean));
        add(listFilterPanel);
    }

    @Override
    public PlanItem createNewChild(final IModel<ProcurementPlan> parentModel) {
        final PlanItem child = new PlanItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PlanItem> item) {
        final PlanItem planItem = item.getModelObject();
        if (planItem.getEditable()) {
            ComponentUtil.addBigDecimalField(item, "estimatedCost").required()
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
            ComponentUtil.addTextField(item, "unitOfIssue").required()
                    .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
            ComponentUtil.addIntegerTextField(item, "quantity").required()
                    .getField().add(RangeValidator.minimum(0));
            ComponentUtil.addBigDecimalField(item, "unitPrice").required()
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
            ComponentUtil.addBigDecimalField(item, "totalCost").required()
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));

            ComponentUtil.addSelect2ChoiceField(item, "procurementMethod", procurementMethodService).required();
            final TextFieldBootstrapFormComponent<String> sourceOfFunds = ComponentUtil.addTextField(item,
                    "sourceOfFunds");
            sourceOfFunds.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
            sourceOfFunds.getField().add(new SourceOfFundsValidator());

            ComponentUtil.addSelect2ChoiceField(item, "targetGroup", targetGroupService);
            ComponentUtil.addBigDecimalField(item, "targetGroupValue").getField().add(RangeValidator.minimum(BigDecimal.ZERO));

            ComponentUtil.addBigDecimalField(item, "quarter1st")
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
            ComponentUtil.addBigDecimalField(item, "quarter2nd")
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
            ComponentUtil.addBigDecimalField(item, "quarter3rd")
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
            ComponentUtil.addBigDecimalField(item, "quarter4th")
                    .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
        } else {
            item.add(new GenericSleepFormComponent<>("estimatedCost"));
            item.add(new GenericSleepFormComponent<>("unitOfIssue"));
            item.add(new GenericSleepFormComponent<>("quantity"));
            item.add(new GenericSleepFormComponent<>("unitPrice"));
            item.add(new GenericSleepFormComponent<>("totalCost"));

            item.add(new GenericSleepFormComponent<>("procurementMethod"));
            item.add(new GenericSleepFormComponent<>("sourceOfFunds"));
            item.add(new GenericSleepFormComponent<>("targetGroup"));
            item.add(new GenericSleepFormComponent<>("targetGroupValue"));

            item.add(new GenericSleepFormComponent<>("quarter1st"));
            item.add(new GenericSleepFormComponent<>("quarter2nd"));
            item.add(new GenericSleepFormComponent<>("quarter3rd"));
            item.add(new GenericSleepFormComponent<>("quarter4th"));
        }
    }

    @Override
    protected Component getHeaderField(final String id, final CompoundPropertyModel<PlanItem> compoundModel) {
        return new PlanItemHeaderPanel(id, compoundModel);
    }

    @Override
    protected BootstrapAddButton getAddNewChildButton() {
        final AddNewChildButton addNewChildButton = new AddNewChildButton("newButton", new ResourceModel("newButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                // make sure that we have cleared the messages.
                final FeedbackMessages feedback = getFeedbackMessages();
                feedback.clear();

                // make all errors visible
                final PanelValidationVisitor panelValidationVisitor = new PanelValidationVisitor(target);
                PlanItemPanel.this.visitChildren(GenericBootstrapFormComponent.class, panelValidationVisitor);

                if (panelValidationVisitor.getFormErrors()) {
                    final ValidationError error = new ValidationError();
                    error.addKey("planItemHasErrors");
                    error(error);
                } else {
                    Boolean descriptionError = false;
                    // check that we have unique descriptions (at least the last element)
                    final List<PlanItem> planItems = PlanItemPanel.this.getModelObject();
                    if (planItems.size() > 2) {
                        final PlanItem lastElement = planItems.get(planItems.size() - 1);
                        for (int i = 0; i < planItems.size() - 1; i++) {
                            final PlanItem planItem = planItems.get(i);

                            if (planItem.getDescription() != null && lastElement.getDescription() != null
                                    && planItem.getDescription().replaceAll("\\s+", "")
                                    .equals(lastElement.getDescription().replaceAll("\\s+", ""))) {
                                descriptionError = true;
                            }
                        }
                    }

                    if (descriptionError) {
                        final ValidationError error = new ValidationError();
                        error.addKey("descriptionError");
                        error(error);
                    } else {
                        super.onSubmit(target);
                    }
                }

                target.add(addButtonNotificationPanel);
            }
        };

        return addNewChildButton;
    }


    @Override
    protected boolean filterListItem(final PlanItem planItem) {
        // don't filter unsaved entities
        if (planItem.getId() == null) {
            return true;
        } else {
            if (listFilterBean.getFilterItem() != null) {
                // it was saved as null when it was saved as draft
                if (planItem.getItem() == null) {
                    return false;
                }
                return planItem.getItem().equals(listFilterBean.getFilterItem());
            }

            return true;
        }
    }

    private class PlanItemHeaderPanel extends GenericPanel<PlanItem> {
        PlanItemHeaderPanel(final String componentId, final IModel<PlanItem> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final PlanItem planItem = getModelObject();
            if (planItem.getEditable()) {
                final Component item = ComponentUtil.addSelect2ChoiceField(this, "item", itemService).required();
                item.add(new StopEventPropagationBehavior());

                final Component description = ComponentUtil.addTextField(this, "description").required();
                description.add(new StopEventPropagationBehavior());
            } else {
                add(new GenericSleepFormComponent<>("item"));
                add(new GenericSleepFormComponent<>("description"));
            }
        }
    }

    private class PlanItemFilterPanel extends GenericPanel<PlanItemFilterBean> {
        PlanItemFilterPanel(final String componentId, final IModel<PlanItemFilterBean> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final Select2ChoiceBootstrapFormComponent<Item> filterItem =
                    ComponentUtil.addSelect2ChoiceField(this, "filterItem", itemService);

            filterItem.getField().add(new AjaxComponentUpdatingBehavior("change"));
        }

        private class AjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
            AjaxComponentUpdatingBehavior(final String event) {
                super(event);
            }

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                // automatically rebuild all ListItems before rendering the list view
                listView.removeAll();
                target.add(listWrapper);
            }
        }
    }

    private class PlanItemFilterBean implements Serializable {
        private Item filterItem;

        public Item getFilterItem() {
            return filterItem;
        }

        public void setFilterItem(final Item filterItem) {
            this.filterItem = filterItem;
        }
    }

    public class SourceOfFundsValidator implements IValidator<String> {
        private static final String ALLOWED_START_CHARACTERS_ZERO = "0";
        private static final String ALLOWED_START_CHARACTERS_ONE = "1";

        public SourceOfFundsValidator() {
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String sourceOfFunds = validatable.getValue();
            if (sourceOfFunds != null) {

                if (!(sourceOfFunds.trim().startsWith(ALLOWED_START_CHARACTERS_ZERO)
                        || sourceOfFunds.trim().startsWith(ALLOWED_START_CHARACTERS_ONE))) {
                    final ValidationError error = new ValidationError(getString("sourceOfFundsStartsWithZero"));
                    validatable.error(error);
                }
            }
        }
    }
}
