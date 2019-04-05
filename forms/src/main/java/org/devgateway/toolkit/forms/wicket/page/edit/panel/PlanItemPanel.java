package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.ItemService;

/**
 * @author idobre
 * @since 2019-04-05
 */
public class PlanItemPanel extends ListViewSectionPanel<PlanItem, ProcurementPlan> {
    @SpringBean
    private ItemService itemService;

    public PlanItemPanel(final String id) {
        super(id);
    }

    @Override
    public PlanItem createNewChild(IModel<ProcurementPlan> parentModel) {
        final PlanItem child = new PlanItem();
        child.setParent(parentModel.getObject());

        return child;
    }

    @Override
    public void populateCompoundListItem(ListItem<PlanItem> item) {
        ComponentUtil.addSelect2ChoiceField(item, "item", itemService, false);
        ComponentUtil.addTextField(item, "description", false);

        ComponentUtil.addLongTextField(item, "estimatedCost", false);
        ComponentUtil.addTextField(item, "unitOfIssue", false);
        ComponentUtil.addIntegerTextField(item, "quantity", false);
        ComponentUtil.addIntegerTextField(item, "unitPrice", false);
        ComponentUtil.addLongTextField(item, "totalCost", false);
    }

    @Override
    protected boolean filterListItem(PlanItem planItem) {
        return true;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
