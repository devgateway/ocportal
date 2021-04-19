package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditSupplierPage;
import org.devgateway.toolkit.forms.wicket.providers.AddNewAdapter;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.select2.ChoiceProvider;

import java.util.Optional;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/prequalifiedSupplierSelector")
public class EditPrequalifiedSupplierSelectorPage extends BasePage {

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @SpringBean
    private SupplierService supplierService;

    @SpringBean
    private PrequalifiedSupplierService prequalifiedSupplierService;

    private LaddaAjaxButton continueBtn;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public EditPrequalifiedSupplierSelectorPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        BootstrapForm<Void> form = new BootstrapForm<>("form");
        add(form);

        PrequalificationYearRange yearRange =
                prequalificationYearRangeService.findById(getPageParameters().get("yearRangeId").toLong()).get();

        Select2ChoiceBootstrapFormComponent<PrequalificationYearRange> yearRangeField =
                new Select2ChoiceBootstrapFormComponent<>(
                        "yearRange",
                        new GenericPersistableJpaTextChoiceProvider<>(prequalificationYearRangeService),
                        Model.of(yearRange));
        yearRangeField.setEnabled(false);
        form.add(yearRangeField);

        ChoiceProvider<Supplier> supplierProvider = new AddNewAdapter<Supplier>(
                new GenericPersistableJpaTextChoiceProvider<>(supplierService)) {

            @Override
            public String getAddNewDisplayValue(String displayValue) {
                return new StringResourceModel("newSupplierItem", EditPrequalifiedSupplierSelectorPage.this)
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

        Select2ChoiceBootstrapFormComponent<Supplier> supplierField =
                new Select2ChoiceBootstrapFormComponent<Supplier>("supplier", supplierProvider, Model.of()) {

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

                        target.add(continueBtn);
                    }
                };
        form.add(supplierField);

        continueBtn = new LaddaAjaxButton("continue", new StringResourceModel("continue"), Buttons.Type.Primary) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(supplierField.getModelObject() != null);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);

                Supplier supplier = supplierField.getModelObject();
                Optional<PrequalifiedSupplier> prequalifiedSupplier =
                        prequalifiedSupplierService.find(supplier, yearRange);

                PageParameters params = new PageParameters();
                if (prequalifiedSupplier.isPresent()) {
                    params.set("id", prequalifiedSupplier.get().getId());
                } else {
                    params.set("yearRangeId", yearRange.getId());
                    params.set("supplierId", supplier.getId());
                }
                setResponsePage(EditPrequalifiedSupplierPage.class, params);
            }
        };
        form.add(continueBtn);
    }
}
