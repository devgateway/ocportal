package org.devgateway.toolkit.forms.service;

import com.google.common.collect.ImmutableMap;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAdministratorReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditInspectionReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditMEReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPMCReportPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPaymentVoucherPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionGroupPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.AwardAcceptanceItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.AwardNotificationItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.BidPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContractDocumentPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PMCMemberPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PMCNotesPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PlanItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchRequisitionPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchaseItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.TenderItemPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

/**
 * @author idobre
 * @since 2019-06-06
 *
 * Class that is used to get the translation label for a field.
 */
public class TranslateField {
    private static Logger logger = LoggerFactory.getLogger(TranslateField.class);

    private final String entitySimpleName;

    private GenericWebPage genericWebPage;

    private Component genericComponent;

    /**
     * Don't use reflection to get the Wicket class for all entities because it's very slow.
     */
    public static final ImmutableMap<String, Class<? extends GenericWebPage<?>>> MAP_BEAN_WICKET_PAGE =
            new ImmutableMap.Builder<String, Class<? extends GenericWebPage<?>>>()
                    .put("ProcurementPlan", EditProcurementPlanPage.class)
                    .put("Project", EditProjectPage.class)
                    .put("CabinetPaper", EditCabinetPaperPage.class)
                    .put("PurchaseRequisitionGroup", EditPurchaseRequisitionGroupPage.class)
                    .put("Tender", EditTenderPage.class)
                    .put("TenderQuotationEvaluation", EditTenderQuotationEvaluationPage.class)
                    .put("ProfessionalOpinion", EditProfessionalOpinionPage.class)
                    .put("AwardNotification", EditAwardNotificationPage.class)
                    .put("AwardAcceptance", EditAwardAcceptancePage.class)
                    .put("Contract", EditContractPage.class)
                    .put("MEReport", EditMEReportPage.class)
                    .put("PMCReport", EditPMCReportPage.class)
                    .put("InspectionReport", EditInspectionReportPage.class)
                    .put("AdministratorReport", EditAdministratorReportPage.class)
                    .put("PaymentVoucher", EditPaymentVoucherPage.class)
                    .build();

    public static final ImmutableMap<String, Class<? extends Component>> MAP_BEAN_WICKET_PANEL =
            new ImmutableMap.Builder<String, Class<? extends Component>>()
                    .put("Bid", BidPanel.class)
                    .put("ContractDocument", ContractDocumentPanel.class)
                    .put("PlanItem", PlanItemPanel.class)
                    .put("PurchaseItem", PurchaseItemPanel.class)
                    .put("TenderItem", TenderItemPanel.class)
                    .put("PurchRequisition", PurchRequisitionPanel.class)
                    .put("ProfessionalOpinionItem", EditProfessionalOpinionPage.class)
                    .put("AwardNotificationItem", AwardNotificationItemPanel.class)
                    .put("AwardAcceptanceItem", AwardAcceptanceItemPanel.class)
                    .put("PMCMember", PMCMemberPanel.class)
                    .put("PMCNotes", PMCNotesPanel.class)
                    .build();

    public TranslateField(final Class entity) {
        this.entitySimpleName = entity.getSimpleName();

        // find the class that will be used to translate fields name
        // first check if entity is a subclass of 'EditAbstractSurvey'.
        final Class<? extends GenericWebPage> translationsWebPage = MAP_BEAN_WICKET_PAGE.get(entitySimpleName);

        // if entity is not a subclass of 'EditAbstractSurvey' then check if it is a subclass of 'GenericPanel'.
        final Class<? extends Component> translationsGenericPanel = MAP_BEAN_WICKET_PANEL.get(entitySimpleName);

        // instantiate the class that will be used to get the field label.
        try {
            if (translationsWebPage != null) {
                genericWebPage = translationsWebPage.getConstructor(PageParameters.class)
                        .newInstance(new PageParameters());
            } else {
                if (translationsGenericPanel != null) {
                    if (BasePage.class.isAssignableFrom(translationsGenericPanel)) {
                        genericComponent = translationsGenericPanel.getConstructor(PageParameters.class)
                                .newInstance(new PageParameters());
                    } else {
                        genericComponent = translationsGenericPanel.getConstructor(String.class).newInstance("id");
                    }
                } else {
                    logger.warn("We didn't found any class for entity: " + this.entitySimpleName);
                }

            }
        } catch (Exception e) {
            logger.error("Instantiation Error", e);
        }
    }

    public String getFieldLabel(final Field field) {
        return getString(field.getName() + ".label");
    }

    public String getString(String key) {
        final Component component = genericWebPage != null ? genericWebPage : genericComponent;
        StringResourceModel stringResourceModel = new StringResourceModel(key, component);
        stringResourceModel.setDefaultValue("");
        final String string = stringResourceModel.getString();

        if (ObjectUtils.isEmpty(string)) {
            logger.warn("We did not find the translation for " + key + " from class: " + this.entitySimpleName);
        }

        return string;
    }
}
