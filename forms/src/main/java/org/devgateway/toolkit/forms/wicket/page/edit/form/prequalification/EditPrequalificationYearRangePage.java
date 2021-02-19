package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationSchemaPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

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

    public TextFieldBootstrapFormComponent<String> createNameField() {
        final TextFieldBootstrapFormComponent<String> name = ComponentUtil.addTextField(editForm, "name");
        ComponentUtil.addUniquePropertyEntryValidator(name.getField(), PrequalificationYearRange_.name,
                jpaService, editForm.getModel(), getString("uniqueName"));
        name.required();
        return name;
    }

    public TextFieldBootstrapFormComponent<Integer> createStartYearField() {
        TextFieldBootstrapFormComponent<Integer> startYear = ComponentUtil.addIntegerTextField(editForm, "startYear");
        startYear.integer().required();
        return startYear;
    }

    public TextFieldBootstrapFormComponent<Integer> createEndYearField() {
        TextFieldBootstrapFormComponent<Integer> endYear = ComponentUtil.addIntegerTextField(editForm, "endYear");
        endYear.integer().required();
        return endYear;
    }

    public Select2ChoiceBootstrapFormComponent<PrequalificationSchema> createSchemaField() {
        GenericPersistableJpaTextChoiceProvider<PrequalificationSchema> schemaProvider =
                new GenericPersistableJpaTextChoiceProvider<>(prequalificationSchemaService);
        schemaProvider.setSpecification((r, cq, cb) ->
                cb.and(cb.equal(r.get(PrequalificationSchema_.status), DBConstants.Status.SUBMITTED)));

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
    }

}
