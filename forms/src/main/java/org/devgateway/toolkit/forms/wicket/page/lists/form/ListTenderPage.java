package org.devgateway.toolkit.forms.wicket.page.lists.form;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.ReleasePackage;
import org.devgateway.ocds.persistence.mongo.repository.main.ReleaseRepository;
import org.devgateway.ocds.web.convert.OCPortalToOCDSConversionServiceImpl;
import org.devgateway.ocds.web.rest.controller.OcdsController;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.TenderFilterState;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tenders")
public class ListTenderPage extends ListAbstractTenderProcessClientEntity<Tender> {
    
    @SpringBean
    protected TenderService tenderService;


    @SpringBean
    private TenderProcessService tenderProcessService;

    @SpringBean
    private ObjectMapper objectMapper;

    @SpringBean
    private OcdsController ocdsController;

    @SpringBean
    private ReleaseRepository releaseRepository;


    public ListTenderPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderService;
        this.editPageClass = EditTenderPage.class;
    }

    public class OcdsPanel extends Panel {
        public OcdsPanel(final String id, final IModel<Long> model) {
            super(id, model);

            Link<Long> downloadLink = new Link<Long>("ocdsLink", model) {
                @Override
                public void onClick() {
                    final AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
                        @Override
                        public void write(final OutputStream output) throws IOException {
                            Optional<TenderProcess> byId = tenderProcessService.findById(model.getObject());
                            Release release = releaseRepository.findByOcid(
                                    OCPortalToOCDSConversionServiceImpl.getOcid(byId.get()));
                            if (release == null) {
                                return;
                            }
                            ReleasePackage releasePackage = ocdsController.createReleasePackage(release);
                            output.write(objectMapper.writeValueAsBytes(releasePackage));
                        }

                        @Override
                        public String getContentType() {
                            return "application/json";
                        }
                    };

                    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rstream,
                            "ocds-purchase-requisition-" + model.getObject() + ".json");
                    handler.setContentDisposition(ContentDisposition.ATTACHMENT);
                    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                }
            };

            downloadLink.add(new Label("ocdsText", "ocds-purchase-requisition-" + model.getObject()));
            downloadLink.add(new TooltipBehavior(
                    new StringResourceModel("downloadOcdsTooltip", ListTenderPage.this, null)));
            add(downloadLink);
        }
    }

    protected void addOcdsDownloadColumn() {
        Component trn = this;
        addFmColumn("downloadOcds", new AbstractColumn<Tender, String>(
                new StringResourceModel("downloadOcds", trn)) {
            @Override
            public void populateItem(final Item<ICellPopulator<Tender>> cellItem, final String componentId,
                                     final IModel<Tender> model) {
                if (DBConstants.Status.EXPORTABLE.contains(model.getObject().getStatus())) {
                    cellItem.add(new ListTenderPage.OcdsPanel(componentId,
                            new Model<>(model.getObject().getTenderProcess().getId())));
                } else {
                    cellItem.add(new Label(componentId));
                }
            }
        });

    }

    @Override
    protected void onInitialize() {

        attachFm("tendersList");

        super.onInitialize();
    }

    @Override
    protected void addColumns() {
        super.addColumns();

        addOcdsDownloadColumn();

        addFmColumn("tenderTitle", new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("title", ListTenderPage.this)).getString()),
                "tenderTitle", "tenderTitle"));

        addFileDownloadColumn();
    }

    @Override
    public JpaFilterState<Tender> newFilterState() {
        return new TenderFilterState();
    }
}
