package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.StopEventPropagationBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.dao.form.PurchRequisition;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisitionGroup;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.persistence.service.category.StaffService;
import org.devgateway.toolkit.persistence.service.form.TenderItemService;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.select2.Select2Choice;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class PurchRequisitionPanel extends ListViewSectionPanel<PurchRequisition, PurchaseRequisitionGroup> {

    @SpringBean
    protected StaffService staffService;

    @SpringBean
    protected ChargeAccountService chargeAccountService;

    @SpringBean
    protected TenderItemService tenderItemService;

    public PurchRequisitionPanel(final String id) {
        super(id);
    }

    protected class PurchRequisitionItemCountValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            List<PurchRequisition> items = PurchRequisitionPanel.this.getModelObject();
            if (items.size() == 0) {
                form.error(getString("atLeastOnePurchRequisition"));
            }

        }
    }
    @Override
    protected BootstrapAddButton getAddNewChildButton() {
        return new AddNewChildButton("newButton", new StringResourceModel("newPurchaseRequisition", this));
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        findParent(Form.class).add(new PurchRequisitionItemCountValidator());
    }

    @Override
    public PurchRequisition createNewChild(final IModel<PurchaseRequisitionGroup> parentModel) {
        final PurchRequisition child = new PurchRequisition();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    @Override
    protected BootstrapDeleteButton getRemoveChildButton(final PurchRequisition item,
                                                         final NotificationPanel removeButtonNotificationPanel) {
        final BootstrapDeleteButton removeButton = new BootstrapDeleteButton("remove",
                new ResourceModel("removeButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                List<TenderItem> tenderItems = null;
                if (!item.isNew()) {
                    tenderItems = tenderItemService.findByPurchaseItemIn(item.getPurchaseItems());
                }

                if (!ObjectUtils.isEmpty(tenderItems)) {
                    final ValidationError error = new ValidationError();
                    error.addKey("purchaseItemError");
                    error(error);
                } else {
                    PurchRequisitionPanel.this.getModelObject().remove(item);
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
    public void populateCompoundListItem(final ListItem<PurchRequisition> item) {
        ComponentUtil.addDateField(item, "requestApprovalDate");
        item.add(new PurchaseItemPanel("purchaseItems"));

        ComponentUtil.addDateField(item, "approvedDate");

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        item.add(formDocs);
    }

    @Override
    protected Component createHeaderField(final String id,
            final CompoundPropertyModel<PurchRequisition> compoundModel) {
        return new PurchRequisitionHeaderPanel(id, compoundModel);
    }

    private class PurchRequisitionHeaderPanel extends GenericPanel<PurchRequisition> {
        PurchRequisitionHeaderPanel(final String componentId, final IModel<PurchRequisition> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            GenericBootstrapFormComponent<Staff, Select2Choice<Staff>> requestedBy =
                    ComponentUtil.addSelect2ChoiceField(PurchRequisitionHeaderPanel.this, "requestedBy",
                            staffService
                    );
            requestedBy.add(new StopEventPropagationBehavior());
            GenericBootstrapFormComponent<ChargeAccount, Select2Choice<ChargeAccount>> chargeAccount =
                    ComponentUtil.addSelect2ChoiceField(PurchRequisitionHeaderPanel.this, "chargeAccount",
                            chargeAccountService
                    );
            chargeAccount.add(new StopEventPropagationBehavior());
        }
    }
}
