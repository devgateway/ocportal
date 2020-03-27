package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.PMCMember;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.category.DesignationService;
import org.devgateway.toolkit.persistence.service.category.PMCStaffService;

public class PMCMemberPanel extends ListViewSectionPanel<PMCMember, PMCReport> {

    @SpringBean
    private PMCStaffService pmcStaffService;

    @SpringBean
    private DesignationService designationService;

    public PMCMemberPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public PMCMember createNewChild(final IModel<PMCReport> parentModel) {
        final PMCMember child = new PMCMember();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PMCMember> item) {
        ComponentUtil.addSelect2ChoiceField(item, "staff", pmcStaffService).required();
        ComponentUtil.addSelect2ChoiceField(item, "designation", designationService).required();
    }

    @Override
    protected boolean filterListItem(final PMCMember member) {
        return true;
    }
}