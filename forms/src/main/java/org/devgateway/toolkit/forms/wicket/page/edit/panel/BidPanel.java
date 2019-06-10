package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.category.SupplierService;

public class BidPanel extends ListViewSectionPanel<Bid, TenderQuotationEvaluation> {
    @SpringBean
    protected SupplierService supplierService;

    private GenericSleepFormComponent supplierID = null;

    public BidPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public Bid createNewChild(final IModel<TenderQuotationEvaluation> parentModel) {
        final Bid child = new Bid();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<Bid> item) {
        Select2ChoiceBootstrapFormComponent<Supplier> supplier = ComponentUtil.addSelect2ChoiceField(item, "supplier",
                supplierService);
        supplier.required();
        supplier.getField().add(new SupplierUpdatingBehavior("change"));
        supplierID = new GenericSleepFormComponent<>("supplierID", (IModel<String>) () -> {
            if (supplier.getModelObject() != null) {
                return supplier.getModelObject().getCode();
            }
            return null;
        });
        supplierID.setOutputMarkupId(true);
        item.add(supplierID);

        ComponentUtil.addIntegerTextField(item, "supplierScore").required()
                .getField().add(RangeValidator.minimum(0));
        ComponentUtil.addIntegerTextField(item, "supplierRanking").required()
                .getField().add(RangeValidator.minimum(0));
        ComponentUtil.addBigDecimalField(item, "quotedAmount")
                .getField().add(RangeValidator.minimum(0.0));

        Select2ChoiceBootstrapFormComponent<String> responsiveness = new Select2ChoiceBootstrapFormComponent<>(
                "supplierResponsiveness",
                new GenericChoiceProvider<>(DBConstants.SupplierResponsiveness.ALL_LIST));
        responsiveness.required();
        item.add(responsiveness);
    }

    @Override
    protected boolean filterListItem(final Bid tenderItem) {
        return true;
    }

    private class SupplierUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        SupplierUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(supplierID);

        }
    }
}