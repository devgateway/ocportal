package org.devgateway.toolkit.forms.wicket.page.edit;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactDropdownButton;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.NewContactAlert;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PrequalifiedSupplierItemListPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.ListPrequalifiedSupplierPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collections;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/prequalifiedSupplier")
public class EditPrequalifiedSupplierPage extends AbstractEditPage<PrequalifiedSupplier> {

    @SpringBean
    private SupplierService supplierService;

    @SpringBean
    private PrequalificationYearRangeService yearRangeService;

    @SpringBean
    private PrequalifiedSupplierService prequalifiedSupplierService;

    public EditPrequalifiedSupplierPage(PageParameters parameters) {
        super(parameters);
        jpaService = prequalifiedSupplierService;
        listPageClass = ListPrequalifiedSupplierPage.class;
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("prequalifiedSupplierForm");

        super.onInitialize();

        IModel<List<SupplierContact>> supplierContactsModel = new LoadableDetachableModel<List<SupplierContact>>() {
            @Override
            protected List<SupplierContact> load() {
                Supplier selectedSupplier = editForm.getModelObject().getSupplier();
                if (selectedSupplier != null) {
                    return selectedSupplier.getContacts();
                } else {
                    return Collections.emptyList();
                }
            }
        };

        PrequalifiedSupplierItemListPanel items;
        items = new PrequalifiedSupplierItemListPanel("items", supplierContactsModel);
        editForm.add(items);

        editForm.add(new Select2ChoiceBootstrapFormComponent<PrequalificationYearRange>("yearRange",
                new GenericPersistableJpaTextChoiceProvider<>(yearRangeService)) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                field.getSettings().setAllowClear(false);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(editForm.getModelObject().isNew());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);

                PrequalifiedSupplier prequalifiedSupplier = editForm.getModelObject();
                PrequalificationYearRange yearRange = prequalifiedSupplier.getYearRange();
                for (PrequalifiedSupplierItem item : prequalifiedSupplier.getItems()) {
                    if (item.getItem() != null && !item.getItem().getParent().equals(yearRange.getSchema())) {
                        item.setItem(null);
                    }
                }
                target.add(items);
            }
        });

        NewContactAlert<PrequalifiedSupplierContact> alert = new NewContactAlert<>("newContactAlert",
                new PropertyModel<>(editForm.getModel(), "contact"), supplierContactsModel);
        editForm.add(alert);

        ContactPanel<SupplierContact> contact = new ContactPanel<SupplierContact>("contact") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                target.add(alert);
            }
        };
        contact.setOutputMarkupId(true);
        editForm.add(contact);

        editForm.add(new ContactDropdownButton<SupplierContact>("selectContact", supplierContactsModel) {

            @Override
            public void onClick(AjaxRequestTarget target, IModel<SupplierContact> model) {
                SupplierContact srcContract = model.getObject();
                PrequalifiedSupplierContact targetContact = editForm.getModelObject().getContact();
                AbstractContact.copy(srcContract, targetContact);

                contact.clearInput();

                target.add(contact);
            }
        });

        Select2ChoiceBootstrapFormComponent<Supplier> supplier;
        supplier = new Select2ChoiceBootstrapFormComponent<Supplier>("supplier",
                new GenericPersistableJpaTextChoiceProvider<>(supplierService)) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);

                send(editForm, Broadcast.BREADTH, new SupplierChanged(target));
            }
        };
        supplier.required();
        editForm.add(supplier);
    }

    public static final class SupplierChanged {

        private final AjaxRequestTarget target;

        public SupplierChanged(AjaxRequestTarget target) {
            this.target = target;
        }

        public AjaxRequestTarget getTarget() {
            return target;
        }
    }

    @Override
    protected PrequalifiedSupplier newInstance() {
        PrequalifiedSupplier prequalifiedSupplier = super.newInstance();
        Long yearRangeId = getPageParameters().get("yearRangeId").toOptionalLong();
        if (yearRangeId != null) {
            prequalifiedSupplier.setYearRange(yearRangeService.findById(yearRangeId).get());
        }
        return prequalifiedSupplier;
    }
}
