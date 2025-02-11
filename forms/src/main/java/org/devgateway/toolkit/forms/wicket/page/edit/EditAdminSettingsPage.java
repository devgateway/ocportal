package org.devgateway.toolkit.forms.wicket.page.edit;


import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.validation.validator.RfcCompliantEmailAddressValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.ocds.web.db.ImportPostgresToMongoJob;
import org.devgateway.ocds.web.spring.SubmittedAlertService;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.cache.CacheManager;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author idobre
 * @since 6/22/16
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/adminsettings")
public class EditAdminSettingsPage extends AbstractEditPage<AdminSettings> {

    private static final long serialVersionUID = 5742724046825803877L;

    private TextFieldBootstrapFormComponent<Integer> excelBatchSize;

    private TextFieldBootstrapFormComponent<Integer> daysSubmittedReminder;

    private CheckBoxToggleBootstrapFormComponent rebootServer;

    private CheckBoxToggleBootstrapFormComponent disableApiSecurity;

    private TextFieldBootstrapFormComponent<String> adminEmail;

    private CheckBoxToggleBootstrapFormComponent enableDailyAutomatedImport;

    private CheckBoxToggleBootstrapFormComponent disableEmailAlerts;

    private TextFieldBootstrapFormComponent<String> importFilesPath;

    private TextFieldBootstrapFormComponent<String> superAdminEmail;

    @SpringBean
    private CacheManager cacheManager;

    @SpringBean
    private SubmittedAlertService submittedAlertService;

    @SpringBean
    private ImportPostgresToMongoJob importPostgresToMongoJob;

    @SpringBean
    private AdminSettingsService adminSettingsService;

    private TextFieldBootstrapFormComponent<Object> autosaveTime;

    public EditAdminSettingsPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = adminSettingsService;
        this.listPageClass = Homepage.class;

        if (entityId == null) {
            final List<AdminSettings> listSettings = adminSettingsService.findAll();
            // just keep 1 entry for settings
            if (listSettings.size() == 1) {
                entityId = listSettings.get(0).getId();
            }
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        editForm.add(new Label("excelTitle", new StringResourceModel("excelTitle", this, null)));


        daysSubmittedReminder = new TextFieldBootstrapFormComponent<>("daysSubmittedReminder");
        daysSubmittedReminder.integer();
        daysSubmittedReminder.getField().add(new RangeValidator(1, 365));
        daysSubmittedReminder.required();
        editForm.add(daysSubmittedReminder);

        excelBatchSize = new TextFieldBootstrapFormComponent<>("excelBatchSize");
        excelBatchSize.integer();
        excelBatchSize.getField().add(new RangeValidator(1, 10000));
        excelBatchSize.required();
        editForm.add(excelBatchSize);

        editForm.add(new Label("systemTitle", new StringResourceModel("systemTitle", this, null)));

//        rebootServer = new CheckBoxToggleBootstrapFormComponent("rebootServer");
//        editForm.add(rebootServer);

        disableApiSecurity = new CheckBoxToggleBootstrapFormComponent("disableApiSecurity");
        editForm.add(disableApiSecurity);

        enableDailyAutomatedImport = new CheckBoxToggleBootstrapFormComponent("enableDailyAutomatedImport");
        enableDailyAutomatedImport.setVisibilityAllowed(false);
        editForm.add(enableDailyAutomatedImport);

        disableEmailAlerts = new CheckBoxToggleBootstrapFormComponent("disableEmailAlerts");
        editForm.add(disableEmailAlerts);

        adminEmail = new TextFieldBootstrapFormComponent<>("adminEmail");
        adminEmail.setVisibilityAllowed(false);
        editForm.add(adminEmail);

        importFilesPath = new TextFieldBootstrapFormComponent<>("importFilesPath");
        importFilesPath.setVisibilityAllowed(false);
        editForm.add(importFilesPath);

        addCacheClearLink();

        autosaveTime = new TextFieldBootstrapFormComponent<>("autosaveTime");
        autosaveTime.integer().required();
        autosaveTime.getField().add(RangeValidator.range(1, 60));
        editForm.add(autosaveTime);

        superAdminEmail = new TextFieldBootstrapFormComponent<>("superAdminEmail");
        editForm.add(superAdminEmail);
        superAdminEmail.required().getField().add(RfcCompliantEmailAddressValidator.getInstance());

        TextFieldBootstrapFormComponent<Integer> unlockAfterHours =
                new TextFieldBootstrapFormComponent<>("unlockAfterHours");
        unlockAfterHours.required();
        unlockAfterHours.integer();
        unlockAfterHours.getField().add(new RangeValidator<>(1, 100));
        editForm.add(unlockAfterHours);

        addImportToMongoLink();

        sendValidatorNotifications();
    }

    private void addCacheClearLink() {
        final AjaxButton link = new AjaxButton("clearCache") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());
            }

        };
        link.add(new AttributeAppender("class", Buttons.Type.Primary));

        link.setLabel(new ResourceModel("clearBackendCaches"));
        editForm.add(link);
    }

    private void addImportToMongoLink() {
        final AjaxButton link = new AjaxButton("importToMongo") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Future<String> stringFuture = importPostgresToMongoJob.importOcdsClientToMongo();
                try {
                    stringFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        link.add(new AttributeAppender("class", Buttons.Type.Primary));

        link.setLabel(new ResourceModel("runMongoImport"));
        editForm.add(link);
    }

    private void sendValidatorNotifications() {
        final AjaxButton link = new AjaxButton("sendValidatorNotifications") {

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Future<String> stringFuture = submittedAlertService.sendNotificationEmails();
                try {
                    stringFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

        };
        link.add(new AttributeAppender("class", Buttons.Type.Primary));

        link.setLabel(new ResourceModel("sendValidatorNotifications"));
        editForm.add(link);
    }
}
