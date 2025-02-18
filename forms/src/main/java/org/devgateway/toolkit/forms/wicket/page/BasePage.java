/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page;

import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.themes.bootstrap.BootstrapCssReference;
import de.agilecoders.wicket.core.util.CssClassNames;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.ColorPickerTextFieldCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.ocds.web.util.SettingsUtils;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.fm.DgFmAttachingVisitor;
import org.devgateway.toolkit.forms.fm.DgFmFormComponentSubject;
import org.devgateway.toolkit.forms.wicket.components.GoogleAnalyticsTracker;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.EditAdminSettingsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractBaseListPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearBudgetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertsStatisticsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListChargeAccountPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListContractDocumentTypePage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListDepartmentPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListDesignationPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListItemPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListPMCStaffPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProcurementMethodRationalePage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProcuringEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProjectClosureHandoverPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListStaffPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSubWardPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSubcountyPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListTargetGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListUnitPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListWardPage;
import org.devgateway.toolkit.forms.wicket.page.lists.feedback.ListFeedbackMessagePage;
import org.devgateway.toolkit.forms.wicket.page.lists.flags.ListFlagHistoryPage;
import org.devgateway.toolkit.forms.wicket.page.lists.fm.ListFeaturesPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAdministratorReportPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListContractPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListInspectionReportPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListMEReportPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPMCReportPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPaymentVoucherPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProjectPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPurchaseRequisitionGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderQuotationEvaluationPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationSchemaPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalifiedSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.LogoutPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.devgateway.toolkit.forms.WebConstants.PARAM_PRINT;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.PMC_METADATA_ROLES;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PROCUREMENT_USER;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_USER;

/**
 * Base wicket-bootstrap {@link org.apache.wicket.Page}
 *
 * @author miha
 */
public abstract class BasePage extends GenericWebPage<Void> implements DgFmFormComponentSubject  {
    private static final long serialVersionUID = -4179591658828697452L;

    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected BootstrapBookmarkablePageLink<Void> printLink;

    private TransparentWebMarkupContainer mainContainer;


    private Footer mainFooter;

    protected Label pageTitle;

    private Navbar navbar;

    protected NotificationPanel feedbackPanel;

    private GoogleAnalyticsTracker googleAnalyticsTracker;

    @SpringBean
    private SettingsUtils settingsUtils;


    protected void createGoogleAnalyticsTracker() {
        googleAnalyticsTracker = new GoogleAnalyticsTracker(
                "googleAnalyticsTracker", settingsUtils.getGoogleAnalyticsTrackingId());
        googleAnalyticsTracker.setVisibilityAllowed(settingsUtils.getGoogleAnalyticsTrackingId() != null);
        add(googleAnalyticsTracker);
    }

    /**
     * Do not allow access to pages that are attached to invisible features
     */
    protected void redirectForInvisibleFm() {
        if (!isFmVisible()) {
            throw new RestartResponseException(Homepage.class);
        }
    }

    @SpringBean
    protected DgFmService fmService;

    @Override
    public DgFmService getFmService() {
        return fmService;
    }

    @Override
    public boolean isEnabled() {
        return isFmEnabled(super::isEnabled);
    }

    @Override
    public boolean isVisible() {
        return isFmVisible(super::isVisible);
    }


    /**
     * Determines if this page has a fluid container for the content or not.
     */
    public Boolean fluidContainer() {
        return true;
    }

    public static class HALRedirectPage extends RedirectPage {
        private static final long serialVersionUID = -750983217518258464L;

        public HALRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/api/browser/");
        }

    }


    public static class UIRedirectPage extends RedirectPage {
        private static final long serialVersionUID = -750983217518258464L;

        public UIRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/portal/");
        }
    }

    /**
     * Selects/changes the default language in the current session. If the
     * {@link WebConstants#LANGUAGE_PARAM} is found in the
     * {@link PageParameters} then its contents is set as language in the
     * session object.
     */
    protected void selectLanguage() {
        StringValue lang = this.getPageParameters().get(WebConstants.LANGUAGE_PARAM);
        if (!lang.isEmpty()) {
            WebSession.get().setLocale(new Locale(lang.toString()));
        }
    }

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public BasePage(final PageParameters parameters) {
        super(parameters);

        selectLanguage();
        add(new HtmlTag("html"));
//        // Add javascript files.
        add(new HeaderResponseContainer("scripts-container", "scripts-bucket"));
        feedbackPanel = createFeedbackPanel();
        add(feedbackPanel);

        mainFooter = new Footer("mainFooter");
        add(mainFooter);
        mainFooter.setVisibilityAllowed(!ComponentUtil.isPrintMode());
        pageTitle = new Label("pageTitle", new ResourceModel("page.title"));
        add(pageTitle);
        mainContainer = new TransparentWebMarkupContainer("mainContainer");
        if (fluidContainer()) {
            mainContainer.add(new CssClassNameAppender(CssClassNames.Grid.containerFluid));
        } else {
            mainContainer.add(new CssClassNameAppender(CssClassNames.Grid.container));
        }
        add(mainContainer);

        add(new Header("mainHeader"));
        createGoogleAnalyticsTracker();
    }


    protected Component createPrintLink(String id) {
        PageParameters pp = new PageParameters(getPageParameters());
        pp.set(PARAM_PRINT, true);
        printLink = new BootstrapBookmarkablePageLink<Void>(id, BasePage.this.getClass(), pp,
                Buttons.Type.Default) {

        };
        printLink.setIconType(FontAwesome5IconType.print_s).setSize(Buttons.Size.Large);
        PopupSettings popupSettings = new PopupSettings(PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS);
        printLink.setPopupSettings(popupSettings);
        add(printLink);
        printLink.setVisibilityAllowed(!ComponentUtil.isPrintMode());
        return printLink;
    }

    private NotificationPanel createFeedbackPanel() {
        final NotificationPanel notificationPanel = new NotificationPanel("feedback");
        notificationPanel.setOutputMarkupId(true);
        return notificationPanel;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        visitChildren(new DgFmAttachingVisitor());
    }

    public NavbarDropDownButton newLanguageMenu() {
        final NavbarDropDownButton languageDropDown =
                new NavbarDropDownButton(new StringResourceModel("navbar.lang", this, null)) {

                    private static final long serialVersionUID = 319842753824102674L;

                    @Override
                    protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                        final List<AbstractLink> list = new ArrayList<>();

                        for (final Locale l : WebConstants.AVAILABLE_LOCALES) {
                            final PageParameters params = new PageParameters(BasePage.this.getPageParameters());
                            params.set(WebConstants.LANGUAGE_PARAM, l.getLanguage());
                            list.add(new MenuBookmarkablePageLink<Page>(BasePage.this.getPageClass(), params,
                                    Model.of(l.getDisplayName())
                            ));
                        }

                        return list;
                    }
                };
        languageDropDown.setIconType(FontAwesome5IconType.flag_r);
        return languageDropDown;
    }



    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);


        // Load Styles.  Use static instances where possible for efficiency.
        response.render(CssHeaderItem.forReference(BootstrapCssReference.instance()));
        response.render(CssHeaderItem.forReference(FontAwesome5CssReference.instance()));
        response.render(CssHeaderItem.forReference(BaseStyles.INSTANCE));

        // Load Scripts.  Respond.js is likely deprecated.  Consider if still needed.
        // If needed, and you have it as a static resource:
        // response.render(JavaScriptHeaderItem.forReference(RespondJavaScriptReference.headerItem()));

        // If Respond.js is a URL:
        // response.render(JavaScriptHeaderItem.forUrl("URL_TO_RESPOND_JS")); // Replace with actual URL.

        // JQuery -  Use a consistent, preferably up-to-date version.
        response.render(JavaScriptHeaderItem.forReference(JQueryResourceReference.getV3()));


    }




}


