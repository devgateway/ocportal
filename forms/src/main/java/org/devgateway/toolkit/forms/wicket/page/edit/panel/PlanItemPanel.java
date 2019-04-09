package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;

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

    public PlanItemPanel(final String id) {
        super(id);
    }

    @Override
    public PlanItem createNewChild(final IModel<ProcurementPlan> parentModel) {
        final PlanItem child = new PlanItem();
        child.setParent(parentModel.getObject());

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PlanItem> item) {
        ComponentUtil.addSelect2ChoiceField(item, "item", itemService).required();
        ComponentUtil.addTextField(item, "description").required();

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
    }

    @Override
    protected boolean filterListItem(final PlanItem planItem) {
        return true;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
