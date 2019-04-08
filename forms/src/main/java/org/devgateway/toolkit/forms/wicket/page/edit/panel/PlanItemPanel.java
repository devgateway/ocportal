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
        ComponentUtil.addSelect2ChoiceField(item, "item", itemService, false);
        ComponentUtil.addTextField(item, "description", false);

        ComponentUtil.addDoubleField(item, "estimatedCost", false);
        ComponentUtil.addTextField(item, "unitOfIssue", false);
        ComponentUtil.addIntegerTextField(item, "quantity", false);
        ComponentUtil.addDoubleField(item, "unitPrice", false);
        ComponentUtil.addDoubleField(item, "totalCost", false);

        ComponentUtil.addSelect2ChoiceField(item, "procurementMethod", procurementMethodService, false);
        ComponentUtil.addTextField(item, "sourceOfFunds", false);
        ComponentUtil.addSelect2ChoiceField(item, "targetGroup", targetGroupService, false);
        ComponentUtil.addDoubleField(item, "targetGroupValue", false);

        ComponentUtil.addDoubleField(item, "quarter1st", false);
        ComponentUtil.addDoubleField(item, "quarter2nd", false);
        ComponentUtil.addDoubleField(item, "quarter3rd", false);
        ComponentUtil.addDoubleField(item, "quarter4th", false);
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
