package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.PMCNotes;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.category.DesignationService;
import org.devgateway.toolkit.persistence.service.category.PMCStaffService;

public class PMCNotesPanel extends ListViewSectionPanel<PMCNotes, PMCReport> {

    @SpringBean
    private PMCStaffService pmcStaffService;

    @SpringBean
    private DesignationService designationService;

    public PMCNotesPanel(final String id) {
        super(id);
    }

    @Override
    public PMCNotes createNewChild(final IModel<PMCReport> parentModel) {
        final PMCNotes child = new PMCNotes();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PMCNotes> item) {
        ComponentUtil.addTextAreaField(item, "text");
    }

    @Override
    protected boolean filterListItem(final PMCNotes member) {
        return true;
    }
}