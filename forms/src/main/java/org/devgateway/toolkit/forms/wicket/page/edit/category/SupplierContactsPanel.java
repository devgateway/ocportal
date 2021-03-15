package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactPanel;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;

/**
 * @author Octavian Ciubotaru
 */
public class SupplierContactsPanel extends ListViewSectionPanel<SupplierContact, Supplier> {

    public SupplierContactsPanel(String id) {
        super(id);
    }

    @Override
    public SupplierContact createNewChild(IModel<Supplier> parentModel) {
        return new SupplierContact(parentModel.getObject());
    }

    @Override
    public void populateCompoundListItem(ListItem<SupplierContact> item) {
        item.add(new ContactPanel<SupplierContact>("contact", item.getModel()) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(findHeaderField(item));
            }
        });
    }

    @Override
    protected Component createHeaderField(String id, CompoundPropertyModel<SupplierContact> compoundModel) {
        Label label = new Label(id, compoundModel);
        label.setOutputMarkupId(true);
        return label;
    }
}
