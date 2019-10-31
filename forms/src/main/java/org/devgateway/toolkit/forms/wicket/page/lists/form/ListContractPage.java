package org.devgateway.toolkit.forms.wicket.page.lists.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ContractDocument;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.ContractFilterState;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.hibernate.Hibernate;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/contracts")
public class ListContractPage extends ListAbstractTenderProcessMakueniEntity<Contract> {
    @SpringBean
    protected ContractService contractService;

    public ListContractPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = contractService;
        this.editPageClass = EditContractPage.class;
    }

    @Override
    protected void onInitialize() {
        addTenderTitleColumn();
        addFileDownloadColumn();
        addAwardeeColumn();
        super.onInitialize();
    }


    public class DownloadPanel extends Panel {
        public DownloadPanel(final String id, final IModel<Contract> model) {
            super(id, model);

            Link<Void> downloadLink = new Link<Void>("downloadLink") {
                @Override
                public void onClick() {
                    final AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                        @Override
                        public void write(final OutputStream output) throws IOException {
                            ZipOutputStream zipOut = new ZipOutputStream(output);
                            for (ContractDocument doc : model.getObject().getContractDocs()) {
                                final FileMetadata file = doc.getFormDoc();
                                Hibernate.initialize(file.getContent());
                                ZipEntry zipEntry = new ZipEntry(file.getName());
                                zipOut.putNextEntry(zipEntry);
                                zipOut.write(file.getContent().getBytes());
                            }
                            zipOut.close();
                        }

                        @Override
                        public String getContentType() {
                            return "application/zip";
                        }
                    };

                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(
                            rstream,
                            "contract-" + model.getObject().getId() + "-documents.zip"
                    );
                    handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
            };

            downloadLink.add(new Label("downloadText", "Download documents"));
            downloadLink.add(new TooltipBehavior(
                    new StringResourceModel("downloadUploadedFileTooltip", ListContractPage.this, null)));
            add(downloadLink);
        }
    }

    @Override
    protected void addFileDownloadColumn() {
        Component trn = this;
        columns.add(new AbstractColumn<Contract, String>(
                new StringResourceModel("downloadFile", trn)) {
            @Override
            public void populateItem(final Item<ICellPopulator<Contract>> cellItem, final String componentId,
                                     final IModel<Contract> model) {

                if (!ObjectUtils.isEmpty(model.getObject().getContractDocs())) {
                    cellItem.add(new DownloadPanel(componentId,  model));
                } else {
                    cellItem.add(new Label(componentId));
                }
            }
        });

    }


    @Override
    public JpaFilterState<Contract> newFilterState() {
        return new ContractFilterState();
    }
}
