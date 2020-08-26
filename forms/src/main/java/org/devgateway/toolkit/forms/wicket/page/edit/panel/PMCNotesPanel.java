package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.PMCNotes;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.category.DesignationService;
import org.devgateway.toolkit.persistence.service.category.PMCStaffService;

import java.util.List;

public class PMCNotesPanel extends ListViewSectionPanel<PMCNotes, PMCReport> {

    @SpringBean
    private PMCStaffService pmcStaffService;

    @SpringBean
    private DesignationService designationService;

    public PMCNotesPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form form = (Form) getParent();
        if (form != null) {
            form.add(new PMCNotesPanel.ListItemsValidator());
        }
    }

    protected class ListItemsValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            List<PMCNotes> notes = PMCNotesPanel.this.getModelObject();
            if (notes.size() == 0) {
                form.error(getString("atLeastOneNotes"));
            }

        }
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
        ComponentUtil.addTextAreaField(item, "notes").required();
    }

    @Override
    protected boolean filterListItem(final PMCNotes member) {
        return true;
    }
}