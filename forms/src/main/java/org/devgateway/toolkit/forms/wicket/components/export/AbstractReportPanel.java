package org.devgateway.toolkit.forms.wicket.components.export;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.AjaxDownloadBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.IResource;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.web.Constants;

import java.io.IOException;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractReportPanel<T> extends Panel {

    private final IModel<T> formModel;

    private BootstrapForm<T> dataExportForm;

    private final AjaxFormListener ajaxFormListener;

    public AbstractReportPanel(String id, AjaxFormListener ajaxFormListener, IModel<T> formModel) {
        super(id);
        this.ajaxFormListener = ajaxFormListener;
        this.formModel = formModel;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(newTitle("title"));

        dataExportForm = new BootstrapForm<>("dataExportForm",
                new CompoundPropertyModel<>(formModel));
        add(dataExportForm);

        AjaxDownloadBehavior excelExportBehavior = new AjaxDownloadBehavior(new AbstractResource() {
            @Override
            protected ResourceResponse newResourceResponse(Attributes attributes) {
                ResourceResponse response = new ResourceResponse();
                response.disableCaching();
                response.setContentType(Constants.ContentType.XLSX);
                response.setFileName(getFileName());
                response.setWriteCallback(new WriteCallback() {
                    @Override
                    public void writeData(Attributes attributes) throws IOException {
                        export(attributes);
                    }
                });

                return response;
            }
        });
        dataExportForm.add(excelExportBehavior);

        final AjaxButton excelButton = new AjaxButton("excelButton",
                new StringResourceModel("export", this)) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setDefaultFormProcessing(true);
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                if (hasData()) {
                    // initiate the file download
                    excelExportBehavior.initiate(target);
                } else {
                    dataExportForm.error(getString("noData"));
                }

                ajaxFormListener.onSubmit(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target) {
                super.onError(target);

                dataExportForm.visitChildren(GenericBootstrapFormComponent.class,
                        new GenericBootstrapValidationVisitor(target));

                ajaxFormListener.onError(target);
            }
        };
        excelButton.add(new AttributeAppender("class", Buttons.Type.Success));
        excelButton.add(new IconBehavior(FontAwesome5IconType.download_s));

        dataExportForm.add(excelButton);
    }

    protected Component newTitle(String id) {
        return new Label(id, new StringResourceModel("title"));
    }

    public BootstrapForm<T> getDataExportForm() {
        return dataExportForm;
    }

    protected abstract String getFileName();

    protected abstract boolean hasData();

    protected abstract void export(IResource.Attributes attributes) throws IOException;
}
