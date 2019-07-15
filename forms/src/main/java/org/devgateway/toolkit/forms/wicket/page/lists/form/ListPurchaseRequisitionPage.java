package org.devgateway.toolkit.forms.wicket.page.lists.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.toolkit.forms.service.MakueniToOCDSConversionService;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.PurchaseRequisitionFilterState;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisitions")
public class ListPurchaseRequisitionPage extends ListAbstractMakueniEntityPage<PurchaseRequisition> {

    @SpringBean
    private MakueniToOCDSConversionService ocdsConversionService;

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    @SpringBean
    private ObjectMapper objectMapper;

    public ListPurchaseRequisitionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = purchaseRequisitionService;
        this.editPageClass = EditPurchaseRequisitionPage.class;
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
                            Optional<PurchaseRequisition> byId = purchaseRequisitionService.findById(model.getObject());

                            Release release = ocdsConversionService.createAndPersistRelease(byId.get());
                            output.write(objectMapper.writeValueAsBytes(release));
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
                    new StringResourceModel("downloadOcdsTooltip", ListPurchaseRequisitionPage.this, null)));
            add(downloadLink);
        }
    }


    protected void addOcdsDownloadColumn() {
        Component trn = this;
        columns.add(new AbstractColumn<PurchaseRequisition, String>(
                new StringResourceModel("downloadOcds", trn)) {
            @Override
            public void populateItem(final Item<ICellPopulator<PurchaseRequisition>> cellItem, final String componentId,
                                     final IModel<PurchaseRequisition> model) {
                if (DBConstants.Status.EXPORTABLE.contains(model.getObject().getStatus())) {
                    cellItem.add(new ListPurchaseRequisitionPage.OcdsPanel(componentId,
                            new Model<>(model.getObject().getId())));
                } else {
                    cellItem.add(new Label(componentId));
                }
            }
        });

    }

    @Override
    protected void onInitialize() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("department", ListPurchaseRequisitionPage.this)).getString()),
                "project.procurementPlan.department", "project.procurementPlan.department",
                new ListModel(departments), dataTable));

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("fiscalYear", ListPurchaseRequisitionPage.this)).getString()),
                "project.procurementPlan.fiscalYear", "project.procurementPlan.fiscalYear",
                new ListModel(fiscalYears), dataTable));

        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("title", ListPurchaseRequisitionPage.this)).getString()),
                "title", "title"));

        columns.add(new PropertyColumn<PurchaseRequisition, String>(
                new Model<>((new StringResourceModel("lastModifiedDate",
                        ListPurchaseRequisitionPage.this)).getString()),
                "lastModifiedDate", "lastModifiedDate") {
            @Override
            public void populateItem(final Item<ICellPopulator<PurchaseRequisition>> item,
                                     final String componentId,
                                     final IModel<PurchaseRequisition> rowModel) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
                final Optional<ZonedDateTime> lastModifiedDate = rowModel.getObject().getLastModifiedDate();

                if (lastModifiedDate.isPresent()) {
                    item.add(new Label(componentId, lastModifiedDate.get().format(formatter)));
                } else {
                    item.add(new Label(componentId, ""));
                }

            }
        });


        addFileDownloadColumn();
        addOcdsDownloadColumn();

        super.onInitialize();
    }


    @Override
    public JpaFilterState<PurchaseRequisition> newFilterState() {
        return new PurchaseRequisitionFilterState();
    }
}
