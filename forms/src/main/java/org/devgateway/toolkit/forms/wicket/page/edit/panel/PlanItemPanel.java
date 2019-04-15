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
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.validators.PanelValidationVisitor;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;

import java.io.Serializable;

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
            ComponentUtil.addDoubleField(item, "estimatedCost").required();
            ComponentUtil.addTextField(item, "unitOfIssue").required();
            ComponentUtil.addIntegerTextField(item, "quantity").required();
            ComponentUtil.addDoubleField(item, "unitPrice").required();
            ComponentUtil.addDoubleField(item, "totalCost").required();

            ComponentUtil.addSelect2ChoiceField(item, "procurementMethod", procurementMethodService).required();
            ComponentUtil.addTextField(item, "sourceOfFunds");
            ComponentUtil.addSelect2ChoiceField(item, "targetGroup", targetGroupService);
            ComponentUtil.addDoubleField(item, "targetGroupValue");

            ComponentUtil.addDoubleField(item, "quarter1st");
            ComponentUtil.addDoubleField(item, "quarter2nd");
            ComponentUtil.addDoubleField(item, "quarter3rd");
            ComponentUtil.addDoubleField(item, "quarter4th");
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
                    super.onSubmit(target);
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
            if (listFilterBean.getItem() != null) {
                // it was saved as null when it was saved as draft
                if (planItem.getItem() == null) {
                    return false;
                }
                return planItem.getItem().equals(listFilterBean.getItem());
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

            final Select2ChoiceBootstrapFormComponent<Item> item =
                    ComponentUtil.addSelect2ChoiceField(this, "item", itemService);

            item.getField().add(new AjaxComponentUpdatingBehavior("change"));
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
        private Item item;

        public Item getItem() {
            return item;
        }

        public void setItem(final Item item) {
            this.item = item;
        }
    }
}
