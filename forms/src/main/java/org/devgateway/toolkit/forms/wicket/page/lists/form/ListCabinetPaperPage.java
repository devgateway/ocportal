package org.devgateway.toolkit.forms.wicket.page.lists.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.CabinetPaperFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/cabinetPapers")
public class ListCabinetPaperPage extends ListAbstractMakueniFormPage<CabinetPaper> {

    @SpringBean
    protected CabinetPaperService cabinetPaperService;

    public ListCabinetPaperPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = cabinetPaperService;
        this.editPageClass = EditCabinetPaperPage.class;
    }

    @Override
    protected void onInitialize() {
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("number", ListCabinetPaperPage.this)).getString()), "number",
                "number"));
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("name", ListCabinetPaperPage.this)).getString()), "name", "name"));

        columns.add(new AbstractColumn<CabinetPaper, String>(
                new StringResourceModel("downloadFile", ListCabinetPaperPage.this)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<CabinetPaper>> cellItem, final String componentId,
                    final IModel<CabinetPaper> model) {
                final FileMetadata file = model.getObject().getFormDocs().stream().findFirst().orElse(null);
                if (file != null) {
                    cellItem.add(new DownloadPanel(componentId, new Model(file)));
                }
            }
        });

        super.onInitialize();

        columns.remove(1); // remove the status column
    }

    @Override
    public JpaFilterState<CabinetPaper> newFilterState() {
        return new CabinetPaperFilterState();
    }

    public class DownloadPanel extends Panel {
        private static final long serialVersionUID = 5821419128121941939L;

        /**
         * @param id
         * @param model
         */
        public DownloadPanel(final String id, final IModel<FileMetadata> model) {
            super(id, model);

            final PageParameters pageParameters = new PageParameters();

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
                    new StringResourceModel("downloadUploadedFileTooltip", ListCabinetPaperPage.this, null)));
            add(downloadLink);
        }
    }
}
