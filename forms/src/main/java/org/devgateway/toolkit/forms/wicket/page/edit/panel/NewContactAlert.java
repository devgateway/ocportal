package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class NewContactAlert<T extends AbstractContact<?>> extends GenericPanel<T> {

    private final IModel<List<SupplierContact>> supplierContactsModel;

    public NewContactAlert(String id, IModel<T> model, IModel<List<SupplierContact>> supplierContactsModel) {
        super(id, model);
        this.supplierContactsModel = supplierContactsModel;
        setOutputMarkupPlaceholderTag(true);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        setVisibilityAllowed(isNew());
    }

    private boolean isNew() {
        T contact = getModelObject();

        if (StringUtils.isEmpty(contact.getDirectors())
                || StringUtils.isEmpty(contact.getEmail())
                || StringUtils.isEmpty(contact.getPhoneNumber())
                || StringUtils.isEmpty(contact.getMailingAddress())) {
            return false;
        }

        for (SupplierContact supplierContact : supplierContactsModel.getObject()) {
            if (supplierContact.getDirectors().equals(contact.getDirectors())
                    && supplierContact.getPhoneNumber().equals(contact.getPhoneNumber())
                    && supplierContact.getEmail().equals(contact.getEmail())
                    && supplierContact.getMailingAddress().equals(contact.getMailingAddress())) {
                return false;
            }
        }

        return true;
    }
}
