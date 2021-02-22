package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.providers.PrequalificationSchemaItemChoiceProvider;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItemContact;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class PrequalifiedSupplierItemListPanel
        extends ListViewSectionPanel<PrequalifiedSupplierItem, PrequalifiedSupplier> {

    private final IModel<List<SupplierContact>> supplierContactsModel;

    public PrequalifiedSupplierItemListPanel(String id, IModel<List<SupplierContact>> supplierContactsModel) {
        super(id);
        this.supplierContactsModel = supplierContactsModel;
    }

    @Override
    public PrequalifiedSupplierItem createNewChild(IModel<PrequalifiedSupplier> parentModel) {
        PrequalifiedSupplierItem item = new PrequalifiedSupplierItem();
        item.setParent(parentModel.getObject());
        return item;
    }

    @Override
    public void populateCompoundListItem(ListItem<PrequalifiedSupplierItem> item) {

        IModel<PrequalificationYearRange> yearRangeModel = item.getModel()
                .map(PrequalifiedSupplierItem::getParent)
                .map(PrequalifiedSupplier::getYearRange);

        item.add(new Select2ChoiceBootstrapFormComponent<PrequalificationSchemaItem>("item",
                new PrequalificationSchemaItemChoiceProvider(yearRangeModel)) {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(findHeaderField(item));
            }
        });

        IModel<Boolean> useDefaultContactModel = new UseDefaultContactModel(item.getModel());

        WebMarkupContainer contactWrapper = new WebMarkupContainer("contactWrapper") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(!useDefaultContactModel.getObject());
            }
        };
        contactWrapper.setOutputMarkupPlaceholderTag(true);
        item.add(contactWrapper);

        NewContactAlert<PrequalifiedSupplierItemContact> alert = new NewContactAlert<>("newContactAlert",
                new PropertyModel<>(item.getModel(), "contact"), supplierContactsModel);
        contactWrapper.add(alert);

        ContactPanel<PrequalifiedSupplierItemContact> contact;
        contact = new ContactPanel<PrequalifiedSupplierItemContact>("contact") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(alert);
            }
        };
        contactWrapper.add(contact);

        contactWrapper.add(new ContactDropdownButton<SupplierContact>("selectContact", supplierContactsModel) {

            @Override
            public void onClick(AjaxRequestTarget target, IModel<SupplierContact> model) {
                SupplierContact srcContract = model.getObject();
                PrequalifiedSupplierItemContact targetContact = item.getModelObject().getContact();
                AbstractContact.copy(srcContract, targetContact);

                contact.clearInput();

                target.add(contactWrapper);
            }
        });

        item.add(new CheckBoxBootstrapFormComponent("useDefaultContact", useDefaultContactModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(contactWrapper);
            }
        });
    }

    @Override
    protected boolean filterListItem(PrequalifiedSupplierItem prequalifiedSupplierItem) {
        return true;
    }

    @Override
    protected Component createHeaderField(String id,
            CompoundPropertyModel<PrequalifiedSupplierItem> compoundModel) {
        Label label = new Label(id, new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                PrequalifiedSupplierItem entity = compoundModel.getObject();
                PrequalificationSchemaItem item = entity.getItem();
                return item == null ? "" : item.toString(entity.getParent().getYearRange());
            }
        });
        label.setOutputMarkupId(true);
        return label;
    }

    private static final class UseDefaultContactModel implements IModel<Boolean> {
        private final IModel<PrequalifiedSupplierItem> model;

        private UseDefaultContactModel(IModel<PrequalifiedSupplierItem> model) {
            this.model = model;
        }

        @Override
        public Boolean getObject() {
            return model.getObject().getContact() == null;
        }

        @Override
        public void setObject(Boolean useDefaultContact) {
            if (useDefaultContact) {
                model.getObject().setContact(null);
            } else {
                model.getObject().setContact(new PrequalifiedSupplierItemContact());
            }
        }
    }
}
