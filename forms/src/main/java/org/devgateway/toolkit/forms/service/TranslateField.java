package org.devgateway.toolkit.forms.service;

import com.google.common.collect.ImmutableMap;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditContractPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderProcessPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderQuotationEvaluationPage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.BidPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContractDocumentPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PlanItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchRequisitionPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchaseItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.TenderItemPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private GenericPanel genericPanel;

    /**
     * Don't use reflection to get the Wicket class for all entities because it's very slow.
     */
    public static final ImmutableMap<String, Class<? extends GenericWebPage>> MAP_BEAN_WICKET_PAGE =
            new ImmutableMap.Builder<String, Class<? extends GenericWebPage>>()
                    .put("ProcurementPlan", EditProcurementPlanPage.class)
                    .put("Project", EditProjectPage.class)
                    .put("CabinetPaper", EditCabinetPaperPage.class)
                    .put("TenderProcess", EditTenderProcessPage.class)
                    .put("Tender", EditTenderPage.class)
                    .put("TenderQuotationEvaluation", EditTenderQuotationEvaluationPage.class)
                    .put("ProfessionalOpinion", EditProfessionalOpinionPage.class)
                    .put("AwardNotification", EditAwardNotificationPage.class)
                    .put("AwardAcceptance", EditAwardAcceptancePage.class)
                    .put("Contract", EditContractPage.class)
                    .build();

    public static final ImmutableMap<String, Class<? extends GenericPanel>> MAP_BEAN_WICKET_PANEL =
            new ImmutableMap.Builder<String, Class<? extends GenericPanel>>()
                    .put("Bid", BidPanel.class)
                    .put("ContractDocument", ContractDocumentPanel.class)
                    .put("PlanItem", PlanItemPanel.class)
                    .put("PurchaseItem", PurchaseItemPanel.class)
                    .put("TenderItem", TenderItemPanel.class)
                    .put("PurchRequisition", PurchRequisitionPanel.class)
                    .build();

    public TranslateField(final Class entity) {
        this.entitySimpleName = entity.getSimpleName();

        // find the class that will be used to translate fields name
        // first check if entity is a subclass of 'EditAbstractSurvey'.
        final Class<? extends GenericWebPage> translationsWebPage = MAP_BEAN_WICKET_PAGE.get(entitySimpleName);

        // if entity is not a subclass of 'EditAbstractSurvey' then check if it is a subclass of 'GenericPanel'.
        final Class<? extends GenericPanel> translationsGenericPanel = MAP_BEAN_WICKET_PANEL.get(entitySimpleName);

        // instantiate the class that will be used to get the field label.
        try {
            if (translationsWebPage != null) {
                genericWebPage = translationsWebPage.getConstructor(PageParameters.class)
                        .newInstance(new PageParameters());
            } else {
                if (translationsGenericPanel != null) {
                    genericPanel = translationsGenericPanel.getConstructor(String.class).newInstance("id");
                } else {
                    logger.error("We didn't found any class for entity: " + this.entitySimpleName);
                }

            }
        } catch (Exception e) {
            logger.error("Instantiation Error", e);
        }
    }

    public String getFieldLabel(final Field field) {
        final Component component = genericWebPage != null ? genericWebPage : genericPanel;
        final String fieldName = field.getName() + ".label";
        final String string = new StringResourceModel(fieldName, component).getString();

        if (string.isEmpty()) {
            logger.error("We did not find the translation for " + fieldName + " from class: " + this.entitySimpleName);
        }

        return string;
    }
}
