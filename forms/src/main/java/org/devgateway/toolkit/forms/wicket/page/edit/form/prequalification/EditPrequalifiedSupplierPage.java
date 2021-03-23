package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.events.SupplierChanged;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactDropdownButton;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.NewContactAlert;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalifiedSupplierPage;
import org.devgateway.toolkit.forms.wicket.providers.AddNewAdapter;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierContact;
import org.devgateway.toolkit.persistence.dao.prequalification.SupplierContact;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.select2.ChoiceProvider;

import java.util.Collections;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
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

        if (!editForm.getModelObject().getYearRange().getSchema().getStatus().equals(DBConstants.Status.SUBMITTED)) {
            throw new RestartResponseException(ListPrequalifiedSupplierPage.class);
        }

        IModel<List<SupplierContact>> supplierContactsModel = editForm.getModel()
                .map(PrequalifiedSupplier::getSupplier)
                .map(Supplier::getContacts)
                .orElse(Collections.emptyList());

        PrequalifiedSupplierItemListPanel items;
        items = new PrequalifiedSupplierItemListPanel("items", supplierContactsModel);
        editForm.add(items);

        Select2ChoiceBootstrapFormComponent<PrequalificationYearRange> yearRange;
        yearRange = new Select2ChoiceBootstrapFormComponent<>("yearRange",
                new GenericPersistableJpaTextChoiceProvider<>(yearRangeService));
        yearRange.setEnabled(false);
        editForm.add(yearRange);

        NewContactAlert<PrequalifiedSupplierContact> alert = new NewContactAlert<>("newContactAlert",
                editForm.getModel().map(PrequalifiedSupplier::getContact), supplierContactsModel);
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

        ChoiceProvider<Supplier> supplierProvider = new AddNewAdapter<Supplier>(
                new GenericPersistableJpaTextChoiceProvider<>(supplierService)) {

            @Override
            public String getAddNewDisplayValue(String displayValue) {
                return new StringResourceModel("newSupplierItem", EditPrequalifiedSupplierPage.this)
                        .setParameters(displayValue)
                        .getString();
            }

            @Override
            public Supplier instantiate(String value) {
                Supplier newSupplier = new Supplier();
                newSupplier.setLabel(value);
                return newSupplier;
            }
        };

        Select2ChoiceBootstrapFormComponent<Supplier> supplier;
        supplier = new Select2ChoiceBootstrapFormComponent<Supplier>("supplier", supplierProvider) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);

                Supplier selectedSupplier = getModelObject();
                if (selectedSupplier.isNew()) {
                    setModelObject(null);

                    PageParameters params = new PageParameters();
                    params.add("label", selectedSupplier.getLabel());
                    CharSequence url = urlFor(EditSupplierPage.class, params);

                    target.appendJavaScript(String.format("window.open('%s');", url));
                }

                send(editForm, Broadcast.BREADTH, new SupplierChanged(target));
            }
        };
        supplier.required();
        editForm.add(supplier);
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
