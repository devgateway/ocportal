package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import static org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService.selectableSpecification;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class EditPrequalificationYearRangePage extends AbstractEditPage<PrequalificationYearRange> {

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;


    @SpringBean
    private PrequalificationSchemaService prequalificationSchemaService;

    public EditPrequalificationYearRangePage(PageParameters parameters) {
        super(parameters);
        this.jpaService = prequalificationYearRangeService;
        this.listPageClass = ListPrequalificationYearRangePage.class;
    }

    protected class YearOrderValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            if (editForm.getModelObject() != null && editForm.getModelObject().getStartYear() != null
                    && editForm.getModelObject().getEndYear() != null
                    && editForm.getModelObject().getStartYear() > editForm.getModelObject().getEndYear()) {
                form.error(getString("yearOrder"));
            }
        }
    }

    protected class NonOverlappingYearValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            if (editForm.getModelObject() != null
                    && prequalificationYearRangeService.countByOverlapping(editForm.getModelObject()) != 0) {
                form.error(getString("overlappingYears"));
            }
        }
    }


    public TextFieldBootstrapFormComponent<String> createNameField() {
        final TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(editForm, "name");
        ComponentUtil.addUniquePropertyEntryValidator(name.getField(), PrequalificationYearRange_.name,
                jpaService, editForm.getModel(), getString("uniqueName"));
        name.required();
        name.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        return name;
    }

    public TextFieldBootstrapFormComponent<Integer> createStartYearField() {
        TextFieldBootstrapFormComponent<Integer> startYear = ComponentUtil.addIntegerTextField(editForm, "startYear");
        startYear.integer().required();
        startYear.add(new RangeValidator<>(1990, 2030));
        return startYear;
    }

    public TextFieldBootstrapFormComponent<Integer> createEndYearField() {
        TextFieldBootstrapFormComponent<Integer> endYear = ComponentUtil.addIntegerTextField(editForm, "endYear");
        endYear.integer().required();
        endYear.add(new RangeValidator<>(1990, 2030));
        return endYear;
    }

    public Select2ChoiceBootstrapFormComponent<PrequalificationSchema> createSchemaField() {
        GenericPersistableJpaTextChoiceProvider<PrequalificationSchema> schemaProvider =
                new GenericPersistableJpaTextChoiceProvider<>(prequalificationSchemaService);
        schemaProvider.setSpecification(selectableSpecification());

        Select2ChoiceBootstrapFormComponent<PrequalificationSchema> schemaField =
                ComponentUtil.addSelect2ChoiceField(editForm, "schema", schemaProvider);
        schemaField.required();

        return schemaField;
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        editForm.add(createNameField());
        editForm.add(createSchemaField());
        editForm.add(createStartYearField());
        editForm.add(createEndYearField());
        editForm.add(new YearOrderValidator());
        editForm.add(new NonOverlappingYearValidator());
    }

}
