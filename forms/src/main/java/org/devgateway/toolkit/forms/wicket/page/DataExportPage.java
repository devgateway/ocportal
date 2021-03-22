package org.devgateway.toolkit.forms.wicket.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.devgateway.toolkit.forms.wicket.components.export.AGPOContractsReportPanel;
import org.devgateway.toolkit.forms.wicket.components.export.AjaxFormListener;
import org.devgateway.toolkit.forms.wicket.components.export.DirectProcurementsAboveReportPanel;
import org.devgateway.toolkit.forms.wicket.components.export.GeneralDepartmentReportPanel;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-06-07
 */
@MountPath("/dataExport")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class DataExportPage extends BasePage implements AjaxFormListener {

    private Component exportPanel;

    public DataExportPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new DropDownButton("exportType", new StringResourceModel("exportType")) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                List<AbstractLink> links = new ArrayList<>();

                links.add(createMenuItem(
                        buttonMarkupId,
                        new StringResourceModel("generalDepartmentExport"),
                        () -> new GeneralDepartmentReportPanel("exportPanel", DataExportPage.this)));

                links.add(createMenuItem(
                        buttonMarkupId,
                        new StringResourceModel("directProcurementsAbove")
                                .setParameters(DBConstants.Reports.DIRECT_PROCUREMENT_THRESHOLD),
                        () -> new DirectProcurementsAboveReportPanel("exportPanel", DataExportPage.this)));

                links.add(createMenuItem(
                        buttonMarkupId,
                        new StringResourceModel("agpoContracts"),
                        () -> new AGPOContractsReportPanel("exportPanel", DataExportPage.this)));

                return links;
            }
        });

        exportPanel = new WebMarkupContainer("exportPanel");
        exportPanel.setOutputMarkupId(true);
        add(exportPanel);
    }

    private AbstractLink createMenuItem(String id, IModel<String> labelModel,
            SerializableSupplier<Component> panelCreator) {
        return new BootstrapAjaxLink<Void>(id, null, Buttons.Type.Link, labelModel) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                exportPanel = exportPanel.replaceWith(panelCreator.get());
                target.add(exportPanel);
            }
        };
    }

    @Override
    public void onSubmit(AjaxRequestTarget target) {
        target.add(feedbackPanel);
    }

    @Override
    public void onError(AjaxRequestTarget target) {
        target.add(feedbackPanel);
    }
}
