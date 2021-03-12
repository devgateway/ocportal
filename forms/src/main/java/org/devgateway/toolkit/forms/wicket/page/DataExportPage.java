package org.devgateway.toolkit.forms.wicket.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.export.GeneralDepartmentReportPanel;
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
public class DataExportPage extends BasePage {

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
                links.add(createGeneralDepartmentExportMenuItem(buttonMarkupId));
                return links;
            }
        });

        exportPanel = new WebMarkupContainer("exportPanel");
        exportPanel.setOutputMarkupId(true);
        add(exportPanel);
    }

    private AbstractLink createGeneralDepartmentExportMenuItem(String id) {
        StringResourceModel labelModel = new StringResourceModel("generalDepartmentExport");
        return new BootstrapAjaxLink<Void>(id, null, Buttons.Type.Link, labelModel) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                exportPanel = exportPanel.replaceWith(new GeneralDepartmentReportPanel("exportPanel") {
                    @Override
                    public void onSubmit(AjaxRequestTarget target) {
                        super.onSubmit(target);
                        target.add(feedbackPanel);
                    }

                    @Override
                    public void onError(AjaxRequestTarget target) {
                        super.onError(target);
                        target.add(feedbackPanel);
                    }
                });
                target.add(exportPanel);
            }
        };
    }
}
