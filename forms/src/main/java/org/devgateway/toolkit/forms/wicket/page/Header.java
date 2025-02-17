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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.page.edit.EditAdminSettingsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractBaseListPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearBudgetPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearPage;
import org.devgateway.toolkit.forms.wicket.page.lists.ListUserPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertsStatisticsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.category.*;
import org.devgateway.toolkit.forms.wicket.page.lists.feedback.ListFeedbackMessagePage;
import org.devgateway.toolkit.forms.wicket.page.lists.flags.ListFlagHistoryPage;
import org.devgateway.toolkit.forms.wicket.page.lists.fm.ListFeaturesPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.*;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationSchemaPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification.ListPrequalifiedSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.user.EditUserPage;
import org.devgateway.toolkit.forms.wicket.page.user.LogoutPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.*;

/**
 * @author idobre
 * @since 12/1/14
 */

public class Header extends Panel {
    private static final long serialVersionUID = 1L;
    @SpringBean
    protected DgFmService fmService;



    public Header(final String markupId) {
        super(markupId);
        Navbar navbar = new Navbar("navbar");
//        navbar.setInverted(true);
        navbar.setPosition(Navbar.Position.TOP);

//        navbar.setBrandImage(new PackageResourceReference(BaseStyles.class, "assets/img/logo.png"),
//                new StringResourceModel("brandImageAltText", this, null));
        navbar.setBrandName(new StringResourceModel("brandName", this, null));

        // Add menu components to the right side of the navbar
        navbar.addComponents(
                NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                        newProcurementFormMenu(),
                        newImplementationFormMenu(),
                        newMetadataMenu(),
                        newAdminMenu(),
                        newMyWorkMenu(),
                        newAccountMenu(),
                        newLogoutMenu()
                )
        );

        add(navbar);
    }

    public Header(final String markupId, final PageParameters parameters) {
        super(markupId);
    }

    private NavbarDropDownButton newImplementationFormMenu() {
        // form menu
        final NavbarDropDownButton formMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.implementationForms", this, null)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();

                addFormMenuItem(list, ListAdministratorReportPage.class,
                        "navbar.administratorReport", "navbar.administratorReport");

                addFormMenuItem(list, ListInspectionReportPage.class,
                        "navbar.inspectionReport", "navbar.inspectionReport");

                addFormMenuItem(list, ListPMCReportPage.class,
                        "navbar.pmcReport", "navbar.pmcReport");

                addFormMenuItem(list, ListMEReportPage.class,
                        "navbar.meReport", "navbar.meReport");

                addFormMenuItem(list, ListPaymentVoucherPage.class,
                        "navbar.paymentVoucher", "navbar.paymentVoucher");

                return list;
            }
        };

        formMenu.setIconType(FontAwesome5IconType.wpforms);
        MetaDataRoleAuthorizationStrategy.authorize(formMenu, Component.RENDER, ROLE_USER);

        return formMenu;
    }



    private <T extends BasePage> void addFormMenuItem(List<AbstractLink> list, Class<T> pageClass, String resourceKey,
                                                      String featureName) {
        StringResourceModel label = new StringResourceModel(resourceKey, this);
        MenuBookmarkablePageLink<T> link = new MenuBookmarkablePageLink<>(pageClass, label);
        link.setIconType(FontAwesome5IconType.file_code_r);

        if (fmService.isFeatureVisible(featureName)) {
            list.add(link);
        }
    }

    private NavbarDropDownButton newAdminMenu() {
        // admin menu
        final NavbarDropDownButton adminMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.admin", this, null)) {
            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();
                BootstrapBookmarkablePageLink<ListUserPage> users =
                        new MenuBookmarkablePageLink<ListUserPage>(ListUserPage.class, null,
                                new StringResourceModel("navbar.users", this, null))
                                .setIconType(FontAwesome5IconType.users_s);
                FormSecurityUtil.authorizeRender(users, ROLE_PMC_ADMIN, ROLE_ADMIN);
                list.add(users);

                list.add(new MenuDivider());

                BootstrapBookmarkablePageLink<Void> adminSettings = new MenuBookmarkablePageLink<Void>(
                        EditAdminSettingsPage.class,
                        new StringResourceModel("navbar.adminSettings", this, null)
                ).setIconType(FontAwesome5IconType.briefcase_s);
                MetaDataRoleAuthorizationStrategy.authorize(adminSettings, Component.RENDER, ROLE_ADMIN);
                list.add(adminSettings);

                BootstrapBookmarkablePageLink<ListAlertPage> feedbackMessages
                        = new MenuBookmarkablePageLink<ListAlertPage>(ListFeedbackMessagePage.class,
                        new StringResourceModel("navbar.feedbackMessages", this, null)
                ).setIconType(FontAwesome5IconType.exclamation_s);
                MetaDataRoleAuthorizationStrategy.authorize(feedbackMessages, Component.RENDER, ROLE_ADMIN);
                list.add(feedbackMessages);


                BootstrapBookmarkablePageLink<ListAlertPage> navbarAlerts = new MenuBookmarkablePageLink<ListAlertPage>(
                        ListAlertPage.class,
                        new StringResourceModel("navbar.alerts", this, null)
                ).setIconType(FontAwesome5IconType.envelope_r);
                MetaDataRoleAuthorizationStrategy.authorize(navbarAlerts, Component.RENDER, ROLE_ADMIN);
                list.add(navbarAlerts);

                createAddFmListMenuWithRole(list, ROLE_ADMIN, ListFlagHistoryPage.class,
                        "navbar.redFlagHistory", FontAwesome5IconType.flag_r);

                BootstrapBookmarkablePageLink<ListAlertsStatisticsPage> alertsStatistics =
                        new MenuBookmarkablePageLink<ListAlertsStatisticsPage>(ListAlertsStatisticsPage.class,
                                new StringResourceModel("navbar.alertsStatistics", this, null)
                        ).setIconType(FontAwesome5IconType.reply_all_s);
                MetaDataRoleAuthorizationStrategy.authorize(alertsStatistics, Component.RENDER, ROLE_ADMIN);
                list.add(alertsStatistics);

                BootstrapBookmarkablePageLink<AllWorkPage> allWork =
                        new MenuBookmarkablePageLink<AllWorkPage>(AllWorkPage.class,
                                new StringResourceModel("navbar.allWork", this, null)
                        ).setIconType(FontAwesome5IconType.briefcase_s);
                MetaDataRoleAuthorizationStrategy.authorize(allWork, Component.RENDER, ROLE_ADMIN);
                list.add(allWork);

                if (WebApplication.get().usesDevelopmentConfig()) {
                    BootstrapBookmarkablePageLink<ListFeaturesPage> features =
                            new MenuBookmarkablePageLink<ListFeaturesPage>(ListFeaturesPage.class,
                                    new StringResourceModel("navbar.features", this, null)
                            ).setIconType(FontAwesome5IconType.list_s);
                    MetaDataRoleAuthorizationStrategy.authorize(features, Component.RENDER, ROLE_ADMIN);
                    list.add(features);
                }

                return list;
            }
        };

        adminMenu.setIconType(FontAwesome5IconType.cog_s);
        FormSecurityUtil.authorizeRender(adminMenu, ROLE_ADMIN, ROLE_PMC_ADMIN);
        return adminMenu;
    }

    private NavbarButton<MyWorkPage> newMyWorkMenu() {
        NavbarButton<MyWorkPage> button = new NavbarButton<>(MyWorkPage.class,
                new StringResourceModel("navbar.myWork", this, null));
        button.setIconType(FontAwesome5IconType.briefcase_s);
        MetaDataRoleAuthorizationStrategy.authorize(button, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        return button;
    }

    private NavbarDropDownButton newProcurementFormMenu() {
        // form menu
        final NavbarDropDownButton formMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.forms", this, null)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();

                addFormMenuItem(list, ListProcurementPlanPage.class,
                        "navbar.procurementPlan", "navbar.procurementPlan");

                addFormMenuItem(list, ListCabinetPaperPage.class,
                        "navbar.cabinetpapers", "navbar.cabinetPapers");

                addFormMenuItem(list, ListProjectPage.class,
                        "navbar.project", "navbar.project");

                addFormMenuItem(list, ListPurchaseRequisitionGroupPage.class,
                        "navbar.tenderProcess", "navbar.tenderProcess");

                addFormMenuItem(list, ListTenderPage.class,
                        "navbar.tenderdocument", "navbar.tenderDocument");

                addFormMenuItem(list, ListTenderQuotationEvaluationPage.class,
                        "navbar.tenderquotationevaluation", "navbar.tenderQuotationEvaluation");

                addFormMenuItem(list, ListProfessionalOpinionPage.class,
                        "navbar.professionalopinion", "navbar.professionalOpinion");

                addFormMenuItem(list, ListAwardNotificationPage.class,
                        "navbar.awardnotification", "navbar.awardNotification");

                addFormMenuItem(list, ListAwardAcceptancePage.class,
                        "navbar.awardacceptance", "navbar.awardAcceptance");

                addFormMenuItem(list, ListContractPage.class,
                        "navbar.contract", "navbar.contract");

                return list;
            }
        };

        formMenu.setIconType(FontAwesome5IconType.wpforms);
        MetaDataRoleAuthorizationStrategy.authorize(formMenu, Component.RENDER, ROLE_USER);

        return formMenu;
    }


    private NavbarButton<Homepage> newHomeMenu() {
        // home
        final NavbarButton<Homepage> homeMenu = new NavbarButton<>(Homepage.class,
                new StringResourceModel("navbar.home", this));
        homeMenu.setIconType(FontAwesome5IconType.home_s);
        MetaDataRoleAuthorizationStrategy.authorize(homeMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);
        return homeMenu;
    }


    private <L extends AbstractBaseListPage<?>> BootstrapBookmarkablePageLink<L>
    createListMenu(Class<L> clazz, String resourceKey, IconType iconType) {
        return new MenuBookmarkablePageLink<L>(clazz, null,
                new StringResourceModel(resourceKey, this, null))
                .setIconType(iconType);
    }

    private <L extends AbstractBaseListPage<?>> BootstrapBookmarkablePageLink<L>
    createAddListMenu(List<AbstractLink> list, Class<L> clazz, String resourceKey, IconType iconType) {

        if (fmService.isFeatureVisible(resourceKey)) {
            BootstrapBookmarkablePageLink<L> menu = new MenuBookmarkablePageLink<L>(clazz, null,
                    new StringResourceModel(resourceKey, this, null))
                    .setIconType(iconType);
            list.add(menu);
            return menu;
        }
        return null;
    }

    private <L extends AbstractBaseListPage<?>> void
    createAddFmListMenuWithRole(List<AbstractLink> list, String role, Class<L> clazz, String resourceKey,
                                IconType iconType) {
        if (fmService.isFeatureVisible(resourceKey)) {
            createAddListMenuWithRole(list, role, clazz, resourceKey, iconType);
        }
    }

    private <L extends AbstractBaseListPage<?>> void
    createAddListMenuWithRole(List<AbstractLink> list, String role, Class<L> clazz, String resourceKey,
                              IconType iconType) {
        BootstrapBookmarkablePageLink<L> menu = createAddListMenu(list, clazz, resourceKey, iconType);
        if (menu != null) {
            MetaDataRoleAuthorizationStrategy.authorize(menu, Component.RENDER, role);
        }
    }

    private <L extends AbstractBaseListPage<?>> void
    createAddListMenuWithRoles(List<AbstractLink> list, Collection<String> roles, Class<L> clazz, String resourceKey,
                               IconType iconType) {
        BootstrapBookmarkablePageLink<L> menu = createAddListMenu(list, clazz, resourceKey, iconType);
        for (String role : roles) {
            MetaDataRoleAuthorizationStrategy.authorize(menu, Component.RENDER, role);
        }
    }



    private NavbarDropDownButton newMetadataMenu() {
        // metadata menu
        final NavbarDropDownButton metadataMenu = new NavbarDropDownButton(
                new StringResourceModel("navbar.metadata", this, null)) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String arg0) {
                final List<AbstractLink> list = new ArrayList<>();


                createAddListMenuWithRole(list, ROLE_ADMIN, ListDepartmentPage.class,
                        "navbar.departments", FontAwesome5IconType.piggy_bank_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListProcurementMethodRationalePage.class,
                        "navbar.procurementMethodRationale", FontAwesome5IconType.bug_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListFiscalYearBudgetPage.class,
                        "navbar.fiscalYearBudget", FontAwesome5IconType.money_bill_alt_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListFiscalYearPage.class,
                        "navbar.fiscalyear", FontAwesome5IconType.calendar_times_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListTargetGroupPage.class,
                        "navbar.targetgroup", FontAwesome5IconType.object_group_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListContractDocumentTypePage.class,
                        "navbar.ContractDocumentType", FontAwesome5IconType.file_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListItemPage.class,
                        "navbar.items", FontAwesome5IconType.list_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListChargeAccountPage.class,
                        "navbar.chargeaccounts", FontAwesome5IconType.money_bill_s
                );

                createAddListMenuWithRole(list, ROLE_PROCUREMENT_USER, ListSupplierPage.class,
                        "navbar.suppliers", FontAwesome5IconType.list_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListStaffPage.class,
                        "navbar.stafflist", FontAwesome5IconType.user_times_s
                );

                createAddListMenuWithRoles(list, PMC_METADATA_ROLES, ListPMCStaffPage.class,
                        "navbar.pmcStaffList", FontAwesome5IconType.user_times_s
                );

                createAddListMenuWithRoles(list, PMC_METADATA_ROLES, ListDesignationPage.class,
                        "navbar.designations", FontAwesome5IconType.certificate_s
                );

                createAddListMenuWithRoles(list, PMC_METADATA_ROLES, ListProjectClosureHandoverPage.class,
                        "navbar.projectClosureHandover", FontAwesome5IconType.list_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListProcuringEntityPage.class,
                        "navbar.procuringentitylist", FontAwesome5IconType.list_s
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListSubcountyPage.class,
                        "navbar.subcountylist", FontAwesome5IconType.flag_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListWardPage.class,
                        "navbar.wardlist", FontAwesome5IconType.flag_r
                );

                createAddListMenuWithRole(list, ROLE_ADMIN, ListSubWardPage.class,
                        "navbar.subwardlist", FontAwesome5IconType.flag_r
                );


                createAddListMenuWithRole(list, ROLE_ADMIN, ListUnitPage.class,
                        "navbar.unitlist", FontAwesome5IconType.list_s
                );

                createAddFmListMenuWithRole(list, ROLE_ADMIN, ListPrequalificationSchemaPage.class,
                        "navbar.prequalificationSchema", FontAwesome5IconType.list_s
                );

                createAddFmListMenuWithRole(list, ROLE_ADMIN, ListPrequalificationYearRangePage.class,
                        "navbar.prequalificationYearRange", FontAwesome5IconType.list_s
                );

                createAddFmListMenuWithRole(list, ROLE_USER, ListPrequalifiedSupplierPage.class,
                        "navbar.prequalifiedSupplierPage", FontAwesome5IconType.list_s
                );


                return list;
            }
        };

        metadataMenu.setIconType(FontAwesome5IconType.code_s);
        MetaDataRoleAuthorizationStrategy.authorize(metadataMenu, Component.RENDER, ROLE_USER);
        return metadataMenu;
    }
    private NavbarButton<EditUserPage> newAccountMenu() {
        final PageParameters pageParametersForAccountPage = new PageParameters();
        final Person person = FormSecurityUtil.getCurrentAuthenticatedPerson();

        // account menu
        Model<String> account = null;
        if (person != null) {
            account = Model.of(person.getFirstName());
            pageParametersForAccountPage.add(WebConstants.PARAM_ID, person.getId());
        }

        final NavbarButton<EditUserPage> accountMenu =
                new NavbarButton<>(EditUserPage.class, pageParametersForAccountPage, account);
        accountMenu.setIconType(FontAwesome5IconType.user_r);
        MetaDataRoleAuthorizationStrategy.authorize(accountMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return accountMenu;
    }


    private NavbarButton<LogoutPage> newLogoutMenu() {
        // logout menu
        final NavbarButton<LogoutPage> logoutMenu =
                new NavbarButton<>(LogoutPage.class, new StringResourceModel("navbar.logout", this, null));
        logoutMenu.setIconType(FontAwesome5IconType.sign_out_alt_s);
        MetaDataRoleAuthorizationStrategy.authorize(logoutMenu, Component.RENDER, SecurityConstants.Roles.ROLE_USER);

        return logoutMenu;
    }
}
