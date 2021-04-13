package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.NonZeroBigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.basic.MultiLineLabel;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BidPanel extends ListViewSectionPanel<Bid, TenderQuotationEvaluation> {
    @SpringBean
    protected SupplierService supplierService;

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @SpringBean
    private PrequalifiedSupplierService prequalifiedSupplierService;

    public BidPanel(final String id) {
        super(id);
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

        GenericSleepFormComponent<String> supplierID =
                new GenericSleepFormComponent<>("supplierID",
                        item.getModel().map(Bid::getSupplier).map(Supplier::getCode));
        supplierID.setOutputMarkupId(true);
        item.add(supplierID);

        GenericSleepFormComponent<String> targetGroup = new GenericSleepFormComponent<>("targetGroup",
                item.getModel().map(Bid::getSupplier).map(Supplier::getTargetGroups).map(Category::categoryLabels));
        targetGroup.setOutputMarkupId(true);
        item.add(targetGroup);

        GenericSleepFormComponent<List<String>> prequalifiedItems =
                new GenericSleepFormComponent<List<String>>("prequalifiedItems",
                        new PrequalifiedItemsModel(item.getModel())) {

                    @Override
                    protected Component newValueComponent(String id) {
                        return new MultiLineLabel(id, getModel());
                    }
                };
        prequalifiedItems.setOutputMarkupId(true);
        item.add(prequalifiedItems);

        supplier.getField().add(new SupplierUpdatingBehavior("change",
                supplierID, targetGroup, prequalifiedItems));

        ComponentUtil.addIntegerTextField(item, "supplierScore")
                .getField().add(RangeValidator.minimum(0));
        ComponentUtil.addIntegerTextField(item, "supplierRanking")
                .getField().add(RangeValidator.minimum(0));

        ComponentUtil.addBigDecimalField(item, "quotedAmount")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new NonZeroBigDecimalValidator());

        Select2ChoiceBootstrapFormComponent<String> responsiveness = new Select2ChoiceBootstrapFormComponent<>(
                "supplierResponsiveness",
                new GenericChoiceProvider<>(DBConstants.SupplierResponsiveness.ALL_LIST));
        item.add(responsiveness);
    }

    private final class PrequalifiedItemsModel extends LoadableDetachableModel<List<String>> {

        private final IModel<Bid> bidModel;

        private PrequalifiedItemsModel(IModel<Bid> bidModel) {
            this.bidModel = bidModel;
        }

        @Override
        protected List<String> load() {
            Bid bid = bidModel.getObject();
            return prequalifiedSupplierService.findItemsForBid(bid);
        }
    }

    private class SupplierUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {

        private final Component[] components;

        SupplierUpdatingBehavior(final String event, Component... components) {
            super(event);
            this.components = components;
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(components);
        }
    }
}
