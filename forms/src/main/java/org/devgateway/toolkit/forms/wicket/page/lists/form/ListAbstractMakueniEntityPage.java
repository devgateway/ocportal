package org.devgateway.toolkit.forms.wicket.page.lists.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListStatusEntityPage;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class ListAbstractMakueniEntityPage<T extends AbstractMakueniEntity>
        extends AbstractListStatusEntityPage<T> {
    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    protected final List<Department> departments;

    protected final List<FiscalYear> fiscalYears;

    public ListAbstractMakueniEntityPage(final PageParameters parameters) {
        super(parameters);

        this.departments = departmentService.findAll();
        this.fiscalYears = fiscalYearService.findAll();
    }

    public class DownloadPanel extends Panel {
        public DownloadPanel(final String id, final IModel<FileMetadata> model) {
            super(id, model);

            Link<FileMetadata> downloadLink = new Link<FileMetadata>("downloadLink", model) {
                @Override
                public void onClick() {
                    final AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                        @Override
                        public void write(final OutputStream output) throws IOException {
                            output.write(getModelObject().getContent().getBytes());
                        }

                        @Override
                        public String getContentType() {
                            return getModelObject().getContentType();
                        }
                    };

                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream,
                            getModelObject().getName());
                    handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
            };

            downloadLink.add(new Label("downloadText", model.getObject().getName()));
            downloadLink.add(new TooltipBehavior(
                    new StringResourceModel("downloadUploadedFileTooltip", ListAbstractMakueniEntityPage.this, null)));
            add(downloadLink);
        }
    }

    @Override
    protected void onInitialize() {
        // just replace the page title with the name of the class
        // instead of having .properties files only for the page title
        addOrReplace(new Label("pageTitle",
                StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                        this.getClass().getSimpleName().replaceAll("List", "").replaceAll("Page", "")), ' ')
                        + " List"));

        super.onInitialize();

        // don't allow users to add new entities from the listing pages for AbstractMakueniEntity.
        editPageLink.setVisibilityAllowed(false);
    }
}
