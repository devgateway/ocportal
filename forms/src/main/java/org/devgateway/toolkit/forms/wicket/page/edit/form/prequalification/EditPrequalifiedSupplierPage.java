package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactDropdownButton;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContactPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.NewContactAlert;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalifiedSupplierPage;
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
        yearRange.required();
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

        Select2ChoiceBootstrapFormComponent<Supplier> supplier;
        supplier = new Select2ChoiceBootstrapFormComponent<>("supplier",
                new GenericPersistableJpaTextChoiceProvider<>(supplierService));
        supplier.required();
        supplier.setEnabled(false);
        editForm.add(supplier);
    }

    @Override
    protected PrequalifiedSupplier newInstance() {
        PrequalifiedSupplier prequalifiedSupplier = super.newInstance();
        Long yearRangeId = getPageParameters().get("yearRangeId").toOptionalLong();
        if (yearRangeId != null) {
            prequalifiedSupplier.setYearRange(yearRangeService.findById(yearRangeId).get());
        }
        Long supplierId = getPageParameters().get("supplierId").toOptionalLong();
        if (supplierId != null) {
            prequalifiedSupplier.setSupplier(supplierService.findById(supplierId).get());
        }
        return prequalifiedSupplier;
    }
}
