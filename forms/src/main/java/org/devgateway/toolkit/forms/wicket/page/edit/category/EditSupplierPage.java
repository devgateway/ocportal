/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSupplierPage;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_PROCUREMENT_USER)
@MountPath("/supplier")
public class EditSupplierPage extends AbstractCategoryEditPage<Supplier> {
    @SpringBean
    private SupplierService supplierService;

    @SpringBean
    private TargetGroupService targetGroupService;

    @SpringBean
    private SubcountyService subcountyService;

    @SpringBean
    private WardService wardService;

    public EditSupplierPage(final PageParameters parameters) {
        super(parameters);
        jpaService = supplierService;
        listPageClass = ListSupplierPage.class;
    }

    @Override
    protected void addCreateLabel() {
        label = new TextFieldBootstrapFormComponent<>("label", LambdaModel.of(editForm.getModel(),
                Supplier::getRealLabel, Supplier::setRealLabel));
        label.required();
        label.getField().add(VALIDATOR);
        editForm.add(label);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (editForm.getModelObject().isNew()) {
            editForm.getModelObject().setLabel(getPageParameters().get("label").toOptionalString());
        }

        addCode();
        final TextAreaFieldBootstrapFormComponent<String> address = ComponentUtil.addTextAreaField(editForm, "address");
        address.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXT);

        ComponentUtil.addSelect2MultiChoiceField(editForm, "targetGroups", targetGroupService);

        Select2MultiChoiceBootstrapFormComponent<Ward> wards;
        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);

        ComponentUtil.addYesNoToggle(editForm, "nonPerforming");

        Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;
        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField()
                .add(new CountyAjaxFormComponentUpdatingBehavior<>(subcounties, wards,
                        LoadableDetachableModel.of(() -> wardService), editForm.getModelObject()::setWards,
                        "change"
                ));

        ComponentUtil.addTextField(editForm, "agpoRegistrationId");

        editForm.add(new SupplierContactsPanel("contacts"));
    }
}
