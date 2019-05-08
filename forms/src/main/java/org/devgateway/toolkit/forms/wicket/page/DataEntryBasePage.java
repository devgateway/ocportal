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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.EditAdminSettingsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListTestFormPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListChargeAccountPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListContractDocumentTypePage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListDepartmentPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListItemPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProcuringEntityPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListStaffPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListTargetGroupPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListContractPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProjectPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPurchaseRequisitionPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListTenderQuotationEvaluationPage;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.LogoutPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.forms.wicket.styles.OverviewStyles;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.references.RespondJavaScriptReference;
import de.agilecoders.wicket.core.markup.html.themes.bootstrap.BootstrapCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;

public abstract class DataEntryBasePage extends GenericWebPage<Void> {
    private static final long serialVersionUID = -4179591658828697452L;

    protected static final Logger logger = LoggerFactory.getLogger(DataEntryBasePage.class);

    private TransparentWebMarkupContainer mainContainer;

    protected NotificationPanel feedbackPanel;

    public static class HALRedirectPage extends RedirectPage {
        private static final long serialVersionUID = -750983217518258464L;

        public HALRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/api/browser/");
        }

    }

    public static class JminixRedirectPage extends RedirectPage {
        private static final long serialVersionUID = -750983217518258464L;

        public JminixRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/jminix/");
        }

    }

    public static class UIRedirectPage extends RedirectPage {
        private static final long serialVersionUID = -750983217518258464L;

        public UIRedirectPage() {
            super(WebApplication.get().getServletContext().getContextPath() + "/ui/index.html");
        }
    }

    /**
     * Selects/changes the default language in the current session. If the
     * {@link WebConstants#LANGUAGE_PARAM} is found in the {@link PageParameters}
     * then its contents is set as language in the session object.
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
    public DataEntryBasePage(final PageParameters parameters) {
        super(parameters);

        selectLanguage();

        add(new HtmlTag("html"));

        // Add javascript files.
        add(new HeaderResponseContainer("scripts-container", "scripts-bucket"));

        feedbackPanel = createFeedbackPanel();
        add(feedbackPanel);

        mainContainer = new TransparentWebMarkupContainer("mainContainer");
        add(mainContainer);
    }

    private NotificationPanel createFeedbackPanel() {
        final NotificationPanel notificationPanel = new NotificationPanel("feedback");
        notificationPanel.setOutputMarkupId(true);
        return notificationPanel;
    }

    private NavbarDropDownButton newLanguageMenu() {
        final NavbarDropDownButton languageDropDown = new NavbarDropDownButton(
                new StringResourceModel("navbar.lang", this, null)) {

            private static final long serialVersionUID = 319842753824102674L;

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                final List<AbstractLink> list = new ArrayList<>();

                for (final Locale l : WebConstants.AVAILABLE_LOCALES) {
                    final PageParameters params = new PageParameters(DataEntryBasePage.this.getPageParameters());
                    params.set(WebConstants.LANGUAGE_PARAM, l.getLanguage());
                    list.add(new MenuBookmarkablePageLink<Page>(DataEntryBasePage.this.getPageClass(), params,
                            Model.of(l.getDisplayName())));
                }

                return list;
            }
        };
        languageDropDown.setIconType(FontAwesomeIconType.flag);
        return languageDropDown;
    }

    private NavbarButton<LogoutPage> newLogoutMenu() {
        // logout menu
        final NavbarButton<LogoutPage> logoutMenu = new NavbarButton<LogoutPage>(LogoutPage.class,
                new StringResourceModel("navbar.logout", this, null));
        logoutMenu.setIconType(FontAwesomeIconType.sign_out);
        MetaDataRoleAuthorizationStrategy.authorize(logoutMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return logoutMenu;
    }

    private NavbarButton<EditUserPage> newAccountMenu() {
        final PageParameters pageParametersForAccountPage = new PageParameters();
        final Person person = WebSecurityUtil.getCurrentAuthenticatedPerson();

        // account menu
        Model<String> account = null;
        if (person != null) {
            account = Model.of(person.getFirstName());
            pageParametersForAccountPage.add(WebConstants.PARAM_ID, person.getId());
        }

        final NavbarButton<EditUserPage> accountMenu = new NavbarButton<>(EditUserPage.class,
                pageParametersForAccountPage, account);
        accountMenu.setIconType(FontAwesomeIconType.user);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return accountMenu;
    }

    private NavbarButton<Homepage> newHomeMenu() {
        // home
        final NavbarButton<Homepage> homeMenu = new NavbarButton<>(Homepage.class, Model.of("Home"));
        homeMenu.setIconType(FontAwesomeIconType.home);
        MetaDataRoleAuthorizationStrategy.authorize(homeMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        return homeMenu;
    }

    private NavbarDropDownButton newMetadataMenu() {
        // metadata menu
        final NavbarDropDownButton metadataMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.metadata", this, null)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();

                list.add(new MenuBookmarkablePageLink<ListDepartmentPage>(ListDepartmentPage.class, null,
                        new StringResourceModel("navbar.departments", this, null))
                                .setIconType(FontAwesomeIconType.bank));

                list.add(new MenuBookmarkablePageLink<ListFiscalYearPage>(ListFiscalYearPage.class, null,
                        new StringResourceModel("navbar.fiscalyear", this, null))
                                .setIconType(FontAwesomeIconType.calendar_times_o));

                list.add(new MenuBookmarkablePageLink<ListTargetGroupPage>(ListTargetGroupPage.class, null,
                        new StringResourceModel("navbar.targetgroup", this, null))
                                .setIconType(FontAwesomeIconType.object_group));

                list.add(new MenuBookmarkablePageLink<ListContractDocumentTypePage>(ListContractDocumentTypePage.class,
                        null, new StringResourceModel("navbar.ContractDocumentType", this, null))
                                .setIconType(FontAwesomeIconType.file));

                list.add(new MenuBookmarkablePageLink<ListItemPage>(ListItemPage.class, null,
                        new StringResourceModel("navbar.items", this, null)).setIconType(FontAwesomeIconType.list));

                list.add(new MenuBookmarkablePageLink<ListChargeAccountPage>(ListChargeAccountPage.class, null,
                        new StringResourceModel("navbar.chargeaccounts", this, null))
                                .setIconType(FontAwesomeIconType.money));

                list.add(new MenuBookmarkablePageLink<ListSupplierPage>(ListSupplierPage.class, null,
                        new StringResourceModel("navbar.suppliers", this, null)).setIconType(FontAwesomeIconType.list));

                list.add(new MenuBookmarkablePageLink<ListStaffPage>(ListStaffPage.class, null,
                        new StringResourceModel("navbar.stafflist", this, null)).setIconType(FontAwesomeIconType.list));

                list.add(new MenuBookmarkablePageLink<ListProcuringEntityPage>(ListProcuringEntityPage.class, null,
                        new StringResourceModel("navbar.procuringentitylist", this, null))
                                .setIconType(FontAwesomeIconType.list));

                return list;
            }
        };

        metadataMenu.setIconType(FontAwesomeIconType.code);
        MetaDataRoleAuthorizationStrategy.authorize(metadataMenu, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

        return metadataMenu;
    }

    private NavbarDropDownButton newFormMenu() {
        // form menu
        final NavbarDropDownButton formMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.forms", this, null)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();

                list.add(new MenuBookmarkablePageLink<ListProcurementPlanPage>(ListProcurementPlanPage.class, null,
                        new StringResourceModel("navbar.procurementPlan", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListCabinetPaperPage>(ListCabinetPaperPage.class, null,
                        new StringResourceModel("navbar.cabinetpapers", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListProjectPage>(ListProjectPage.class, null,
                        new StringResourceModel("navbar.project", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListPurchaseRequisitionPage>(ListPurchaseRequisitionPage.class,
                        null, new StringResourceModel("navbar.purchaseRequisition", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListTenderPage>(ListTenderPage.class, null,
                        new StringResourceModel("navbar.tenderdocument", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListTenderQuotationEvaluationPage>(
                        ListTenderQuotationEvaluationPage.class, null,
                        new StringResourceModel("navbar.tenderquotationevaluation", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListProfessionalOpinionPage>(ListProfessionalOpinionPage.class,
                        null, new StringResourceModel("navbar.professionalopinion", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListAwardNotificationPage>(ListAwardNotificationPage.class, null,
                        new StringResourceModel("navbar.awardnotification", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));
                list.add(new MenuBookmarkablePageLink<ListAwardAcceptancePage>(ListAwardAcceptancePage.class, null,
                        new StringResourceModel("navbar.awardacceptance", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                list.add(new MenuBookmarkablePageLink<ListContractPage>(ListContractPage.class, null,
                        new StringResourceModel("navbar.contract", this, null))
                                .setIconType(FontAwesomeIconType.file_text_o));

                return list;
            }
        };

        formMenu.setIconType(FontAwesomeIconType.wpforms);
        MetaDataRoleAuthorizationStrategy.authorize(formMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return formMenu;
    }

    private NavbarDropDownButton newAdminMenu() {
        // admin menu
        final NavbarDropDownButton adminMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.admin", this, null)) {
            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();
                list.add(new MenuBookmarkablePageLink<ListTestFormPage>(ListUserPage.class, null,
                        new StringResourceModel("navbar.users", this, null)).setIconType(FontAwesomeIconType.users));

                list.add(new MenuBookmarkablePageLink<ListTestFormPage>(ListTestFormPage.class, null,
                        new StringResourceModel("navbar.testcomponents", this, null))
                                .setIconType(FontAwesomeIconType.android));

                list.add(new MenuDivider());

                final BootstrapBookmarkablePageLink swagger = new MenuBookmarkablePageLink<Void>(SwaggerPage.class,
                        new StringResourceModel("navbar.swagger", DataEntryBasePage.this, null))
                                .setIconType(FontAwesomeIconType.code);
                MetaDataRoleAuthorizationStrategy.authorize(swagger, Component.RENDER,
                        SecurityConstants.Roles.ROLE_ADMIN);
                list.add(swagger);

                list.add(new MenuBookmarkablePageLink<SpringEndpointsPage>(SpringEndpointsPage.class, null,
                        new StringResourceModel("navbar.springendpoints", this, null))
                                .setIconType(FontAwesomeIconType.anchor));

                list.add(new MenuBookmarkablePageLink<JminixRedirectPage>(JminixRedirectPage.class, null,
                        new StringResourceModel("navbar.jminix", this, null)).setIconType(FontAwesomeIconType.bug));

                final MenuBookmarkablePageLink<HALRedirectPage> hlLink = new MenuBookmarkablePageLink<HALRedirectPage>(
                        HALRedirectPage.class, null, new StringResourceModel("navbar.halbrowser", this, null)) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("target", "_blank");
                    }
                };
                hlLink.setIconType(FontAwesomeIconType.rss).setEnabled(true);

                list.add(hlLink);

                final MenuBookmarkablePageLink<UIRedirectPage> uiLink = new MenuBookmarkablePageLink<UIRedirectPage>(
                        UIRedirectPage.class, null, new StringResourceModel("navbar.ui", this, null)) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("target", "_blank");
                    }
                };
                uiLink.setIconType(FontAwesomeIconType.rocket).setEnabled(true);
                list.add(uiLink);

                list.add(new MenuDivider());

                list.add(new MenuBookmarkablePageLink<Void>(EditAdminSettingsPage.class,
                        new StringResourceModel("navbar.adminSettings", DataEntryBasePage.this, null))
                                .setIconType(FontAwesomeIconType.briefcase));

                return list;
            }
        };

        adminMenu.setIconType(FontAwesomeIconType.cog);
        MetaDataRoleAuthorizationStrategy.authorize(adminMenu, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);

        return adminMenu;
    }

    /**
     * creates a new {@link Navbar} instance
     *
     * @param markupId The components markup id.
     * @return a new {@link Navbar} instance
     */
    protected Navbar newNavbar(final String markupId) {

        Navbar navbar = new Navbar(markupId);

        /**
         * Make sure to update the BaseStyles when the navbar position changes.
         *
         * @see org.devgateway.toolkit.forms.wicket.styles.BaseStyles
         */
        navbar.setPosition(Navbar.Position.TOP);
        navbar.setInverted(true);

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, newHomeMenu(), newFormMenu(),
                newMetadataMenu(), newAdminMenu(), newAccountMenu(), newLogoutMenu()));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, newLanguageMenu()));

        return navbar;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        // Load Styles.
        response.render(CssHeaderItem.forReference(BaseStyles.INSTANCE));
        response.render(CssHeaderItem.forReference(BootstrapCssReference.instance()));
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
        response.render(CssHeaderItem.forReference(OverviewStyles.INSTANCE));

        // Load Scripts.
        response.render(RespondJavaScriptReference.headerItem());
        response.render(JavaScriptHeaderItem.forReference(JQueryResourceReference.getV2()));

        // file upload improvement script
        response.render(JavaScriptHeaderItem
                .forReference(new JavaScriptResourceReference(BaseStyles.class, "assets/js/fileupload.js")));
    }
}
