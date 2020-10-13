package org.devgateway.toolkit.forms.wicket.components.util;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxYesNoToggleBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.DateTimeFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.PasswordFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextAreaFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.events.EditingEnabledEvent;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.EditorValidatorRoleAssignable;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.select2.ChoiceProvider;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-04
 */
public final class ComponentUtil {
    private static final DecimalFormat DF = new DecimalFormat("#,###.###");

    protected static final Logger logger = LoggerFactory.getLogger(ComponentUtil.class);

    private ComponentUtil() {

    }

    public static AJAXDownload createAJAXDownload(String filePath, String contentType, Class<?> caller) {
        return new AJAXDownload() {
            @Override
            protected IRequestHandler getHandler() {
                return new IRequestHandler() {
                    @Override
                    public void respond(final IRequestCycle requestCycle) {
                        final HttpServletResponse response = (HttpServletResponse) requestCycle
                                .getResponse().getContainerResponse();
                        try {
                            InputStream in = caller.getClassLoader().getResourceAsStream(filePath);
                            response.setContentType(contentType);
                            response.setHeader("Content-Disposition", "attachment; filename=" + filePath);
                            IOUtils.copy(in, response.getOutputStream());
                            in.close();
                        } catch (IOException e) {
                            logger.error("Download error", e);
                        }
                        RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);
                    }

                    @Override
                    public void detach(final IRequestCycle requestCycle) {
                        // do nothing;
                    }
                };
            }
        };
    }

    public static List<Supplier> getSuppliersInTenderQuotation(TenderProcess tenderProcess, boolean filterPass) {
        final TenderQuotationEvaluation tenderQuotationEvaluation =
                PersistenceUtil.getNext(tenderProcess.getTenderQuotationEvaluation());
        final List<Supplier> suppliers = new ArrayList<>();
        if (tenderQuotationEvaluation != null && !tenderQuotationEvaluation.getBids().isEmpty()) {
            for (Bid bid : tenderQuotationEvaluation.getBids()) {
                if (bid.getSupplier() != null) {
                    if (filterPass && !DBConstants.SupplierResponsiveness.PASS.equalsIgnoreCase(
                            bid.getSupplierResponsiveness())) {
                        continue;
                    }
                    suppliers.add(bid.getSupplier());
                }
            }
        }
        return suppliers;
    }

    public static Date getDateFromLocalDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean canAccessAddNewButtons(Class<? extends AbstractEditPage<?>> editClazz,
                                                 PermissionEntityRenderableService permissionEntityRenderableService,
                                                 SessionMetadataService sessionMetadataService) {
        PageProvider pageProvider = new PageProvider(editClazz);
        IRequestablePage editPage = pageProvider.getPageInstance();
        String allowedAccess = permissionEntityRenderableService.getAllowedAccess((EditorValidatorRoleAssignable)
                editPage, true, sessionMetadataService.getSessionDepartment());
        return SecurityConstants.Action.EDIT.equals(allowedAccess);
    }

    /**
     * Returns true if the {@link WebConstants#PARAM_PRINT} is used as a parameter
     *
     * @return
     */
    public static boolean isPrintMode() {
        return RequestCycle.get().getRequest().getRequestParameters().getParameterValue(WebConstants.PARAM_PRINT)
                .toBoolean(false);
    }

    public static void enableDisableEvent(final Component c, final IEvent<?> event) {
        if (event.getPayload() instanceof EditingDisabledEvent) {
            c.setEnabled(false);
        }

        if (event.getPayload() instanceof EditingEnabledEvent) {
            c.setEnabled(true);
        }

    }

    public static IValidator<? super String> isEmail() {
        return EmailAddressValidator.getInstance();
    }

    public static CheckBoxBootstrapFormComponent addCheckBox(
            final WebMarkupContainer parent,
            final String id) {
        final CheckBoxBootstrapFormComponent checkBox = new CheckBoxBootstrapFormComponent(id);
        parent.add(checkBox);

        return checkBox;
    }

    public static CheckBoxToggleBootstrapFormComponent addCheckToggle(
            final WebMarkupContainer parent,
            final String id) {
        final CheckBoxToggleBootstrapFormComponent checkToggle = new CheckBoxToggleBootstrapFormComponent(id);
        parent.add(checkToggle);

        return checkToggle;
    }

    public static CheckBoxYesNoToggleBootstrapFormComponent addYesNoToggle(
            final WebMarkupContainer parent,
            final String id) {
        final CheckBoxYesNoToggleBootstrapFormComponent checkToggle = new CheckBoxYesNoToggleBootstrapFormComponent(id);
        parent.add(checkToggle);

        return checkToggle;
    }

    public static CheckBoxYesNoToggleBootstrapFormComponent addYesNoToggle(
            final WebMarkupContainer parent,
            final String id, Boolean removeCheckboxClass) {
        final CheckBoxYesNoToggleBootstrapFormComponent checkToggle = new CheckBoxYesNoToggleBootstrapFormComponent(
                id, removeCheckboxClass
        );
        parent.add(checkToggle);

        return checkToggle;
    }

    public static FormComponent<?>[] getFormComponentsFromBootstrapComponents(List<GenericBootstrapFormComponent<?, ?>>
                                                                                      bc) {
        return bc.stream().map(GenericBootstrapFormComponent::getField).toArray(FormComponent[]::new);
    }

    public static TextAreaFieldBootstrapFormComponent<String> addTextAreaField(
            final WebMarkupContainer parent,
            final String id) {
        final TextAreaFieldBootstrapFormComponent<String> textAreaField = new TextAreaFieldBootstrapFormComponent<>(id);
        parent.add(textAreaField);

        return textAreaField;
    }

    public static TextFieldBootstrapFormComponent<String> addTextField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<>(id);
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<String> addTextLoginField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<String> textField = new TextFieldBootstrapFormComponent<String>(id) {
            @Override
            public String getUpdateEvent() {
                return null;
            }
        };
        parent.add(textField);

        return textField;
    }

    public static PasswordFieldBootstrapFormComponent addTextPasswordField(
            final WebMarkupContainer parent,
            final String id) {
        final PasswordFieldBootstrapFormComponent textField = new PasswordFieldBootstrapFormComponent(id) {
            @Override
            public String getUpdateEvent() {
                return null;
            }
        };
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<Integer> addIntegerTextField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<Integer> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.integer();
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<Long> addLongTextField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<Long> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.longValue();
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<BigDecimal> addBigDecimalBudgetAmountField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<BigDecimal> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.decimal();
        textField.getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<BigDecimal> addBigDecimalField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<BigDecimal> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.decimal();
        parent.add(textField);

        return textField;
    }

    public static TextFieldBootstrapFormComponent<Double> addDoubleField(
            final WebMarkupContainer parent,
            final String id) {
        final TextFieldBootstrapFormComponent<Double> textField = new TextFieldBootstrapFormComponent<>(id);
        textField.asDouble();
        parent.add(textField);

        return textField;
    }

    public static DateTimeFieldBootstrapFormComponent addDateTimeField(
            final WebMarkupContainer parent,
            final String id) {
        final DateTimeFieldBootstrapFormComponent field = new DateTimeFieldBootstrapFormComponent(id);
        parent.add(field);

        return field;
    }

    public static DateFieldBootstrapFormComponent addDateField(
            final WebMarkupContainer parent,
            final String id) {
        final DateFieldBootstrapFormComponent field = new DateFieldBootstrapFormComponent(id);
        parent.add(field);

        return field;
    }

    public static <E extends GenericPersistable & Labelable & Serializable> Select2ChoiceBootstrapFormComponent<E>
    addSelect2ChoiceField(
            final WebMarkupContainer parent,
            final String id,
            ChoiceProvider<E> choiceProvider) {
        final Select2ChoiceBootstrapFormComponent<E> component = new Select2ChoiceBootstrapFormComponent<E>(
                id,
                choiceProvider
        );
        parent.add(component);

        return component;
    }

    public static <E extends GenericPersistable & Labelable & Serializable> Select2ChoiceBootstrapFormComponent<E>
    addSelect2ChoiceField(
            final WebMarkupContainer parent,
            final String id,
            final TextSearchableService<E> searchService) {
        final GenericPersistableJpaTextChoiceProvider<E> choiceProvider
                = new GenericPersistableJpaTextChoiceProvider<>(searchService);
        final Select2ChoiceBootstrapFormComponent<E> component = new Select2ChoiceBootstrapFormComponent<>(
                id,
                choiceProvider
        );
        parent.add(component);

        return component;
    }

    public static <E extends GenericPersistable & Labelable & Serializable> Select2MultiChoiceBootstrapFormComponent<E>
    addSelect2MultiChoiceField(
            final WebMarkupContainer parent,
            final String id,
            final TextSearchableService<E> searchService) {
        final GenericPersistableJpaTextChoiceProvider<E> choiceProvider =
                new GenericPersistableJpaTextChoiceProvider<>(searchService);
        final Select2MultiChoiceBootstrapFormComponent<E> component =
                new Select2MultiChoiceBootstrapFormComponent<>(id, choiceProvider);
        parent.add(component);

        return component;
    }

    /**
     * Trivial method to set the child {@link GenericBootstrapFormComponent}
     * required when added to the parent {@link WebMarkupContainer}
     *
     * @param requiredFlag the {@link FormComponent#setRequired(boolean)}
     * @param parent
     * @param child
     * @return the parent
     */
    public static MarkupContainer addRequiredFlagBootstrapFormComponent(
            final boolean requiredFlag,
            final WebMarkupContainer parent,
            final GenericBootstrapFormComponent<?, ?> child) {
        return parent.add(requiredFlag ? child.required() : child);
    }

    public static String formatNumber(final BigDecimal number) {
        return DF.format(number);
    }
}
