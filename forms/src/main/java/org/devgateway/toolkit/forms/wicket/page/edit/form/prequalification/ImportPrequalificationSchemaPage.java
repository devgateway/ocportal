package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.Serializable;

@MountPath
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class ImportPrequalificationSchemaPage extends BasePage {

    @SpringBean
    private PrequalificationSchemaService prequalificationSchemaService;
    private Select2ChoiceBootstrapFormComponent<PrequalificationSchema> schemaField;

    /**
     * Construct.
     *
     * @param parameters                    current page parameters
     * @param prequalificationSchemaService
     */
    public ImportPrequalificationSchemaPage(PageParameters parameters, PrequalificationSchemaService prequalificationSchemaService) {
        super(parameters);
        this.prequalificationSchemaService = prequalificationSchemaService;
    }

    public static class SchemaSelect implements Serializable {
        private PrequalificationSchema schema;

        public PrequalificationSchema getSchema() {
            return schema;
        }

        public void setSchema(PrequalificationSchema schema) {
            this.schema = schema;
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final BootstrapBookmarkablePageLink<Void> newSchema = new BootstrapBookmarkablePageLink<>(
                "newSchema", EditPrequalificationSchemaPage.class, Buttons.Type.Success);
        newSchema.setIconType(FontAwesome5IconType.plus_circle_s).setSize(Buttons.Size.Large);
        newSchema.setLabel(new ResourceModel("newSchema"));
        add(newSchema);


        BootstrapForm<SchemaSelect> form = new BootstrapForm<>("form",
                new CompoundPropertyModel<>(LoadableDetachableModel.of(SchemaSelect::new)));
        add(form);

        final BootstrapSubmitButton importSchema = new BootstrapSubmitButton(
                "importSchema", new ResourceModel("importSchema")) {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Long newSchemaId = prequalificationSchemaService.saveDuplicateSchema(
                        schemaField.getField().getModelObject().getId());
                PageParameters pp = new PageParameters();
                pp.set(WebConstants.PARAM_ID, newSchemaId);
                setResponsePage(EditPrequalificationSchemaPage.class, pp);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(form);
            }
        };
        importSchema.add(new IconBehavior(FontAwesome5IconType.upload_s));
        importSchema.add(new AttributeAppender("size", Buttons.Size.Large));

        form.add(importSchema);


        GenericPersistableJpaTextChoiceProvider<PrequalificationSchema> schemaProvider =
                new GenericPersistableJpaTextChoiceProvider<>(prequalificationSchemaService);
        schemaProvider.setSpecification((r, cq, cb) ->
                cb.and(cb.equal(r.get(PrequalificationSchema_.status), DBConstants.Status.SUBMITTED)));

        schemaField = ComponentUtil.addSelect2ChoiceField(form, "schema", schemaProvider);
        schemaField.required();
    }
}
