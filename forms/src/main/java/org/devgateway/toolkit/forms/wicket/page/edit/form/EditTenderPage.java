package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.fm.DgFmBehavior;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.validators.EarlierThanDateFieldValidator;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.TenderItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodRationaleService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.ProcuringEntityService;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditTenderPage extends EditAbstractTenderProcessClientEntityPage<Tender>
        implements ProcurementRoleAssignable {

    @SpringBean
    protected TenderService tenderService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    @SpringBean
    protected ProcurementMethodService procurementMethodService;

    @SpringBean
    protected SubcountyService subcountyService;

    @SpringBean
    protected WardService wardService;

    @SpringBean
    protected ProcurementMethodRationaleService procurementMethodRationaleService;


    @SpringBean
    protected ProcuringEntityService procuringEntityService;

    @SpringBean
    private TargetGroupService targetGroupService;

    private GenericSleepFormComponent<String> procuringEntityEmail;

    private GenericSleepFormComponent<String> procuringEntityAddress;

    private Select2MultiChoiceBootstrapFormComponent<Ward> wards;
    private Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;

    public EditTenderPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderService;
    }

    public EditTenderPage() {
        this(new PageParameters());
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("tenderForm");
        super.onInitialize();
        final TextFieldBootstrapFormComponent<String> title = ComponentUtil.addTextField(editForm, "tenderTitle");
        //title.required();
        title.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);

        title.getField().add(new UniquePropertyEntryValidator<>(getString("uniqueTitle"), tenderService::findAll,
                (o, v) -> (root, query, cb) -> cb.equal(cb.lower(root.get(Tender_.tenderTitle)), v.toLowerCase()),
                editForm.getModel()
        ));

        final TextFieldBootstrapFormComponent<String> tenderNumber = ComponentUtil.addTextField(
                editForm,
                "tenderNumber"
        );
        //tenderNumber.required();
        tenderNumber.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        tenderNumber.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueNumber"),
                tenderService::findAll,
                (o, v) -> (root, query, cb) -> cb.equal(cb.lower(root.get(Tender_.tenderNumber)), v.toLowerCase()),
                editForm.getModel()
        ));

        DateFieldBootstrapFormComponent closingDate = ComponentUtil.addDateField(editForm, "closingDate");
        FiscalYear fiscalYear = editForm.getModelObject().getProcurementPlan().getFiscalYear();

        //closingDate.required();

        final DateFieldBootstrapFormComponent invitationDate = ComponentUtil.addDateField(editForm, "invitationDate");
        //invitationDate.required();
        invitationDate.getField().add(RangeValidator.range(fiscalYear.getStartDate(), fiscalYear.getEndDate()));
        invitationDate.getField().add(new EarlierThanDateFieldValidator(closingDate,
                getString("closingDate.label")));

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementMethod", procurementMethodService);

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementMethodRationale",
                procurementMethodRationaleService);

        ComponentUtil.addTextAreaField(editForm, "objective").getField()
                .add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXTAREA);

        addProcuringEntitySection();

        ComponentUtil.addBigDecimalField(editForm, "tenderValue")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());

        ComponentUtil.addSelect2ChoiceField(editForm, "targetGroup", targetGroupService);

        editForm.add(new TenderItemPanel("tenderItems"));

        final TextFieldBootstrapFormComponent<String> tenderLink = ComponentUtil.addTextField(editForm, "tenderLink");
        tenderLink.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_ONE_LINE_TEXTAREA);
        tenderLink.getField().add(tenderDocOrTenderLinkRequiredValidator());
        tenderLink.getField().add(new UrlValidator());

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        editForm.add(formDocs);

        FileInputBootstrapFormComponent billOfQuantities = new FileInputBootstrapFormComponent("billOfQuantities");
        billOfQuantities.setShowTooltip(true);
        editForm.add(billOfQuantities);

        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);
        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField().add(new CountyAjaxFormComponentUpdatingBehavior<>(subcounties, wards,
                LoadableDetachableModel.of(() -> wardService), editForm.getModelObject()::setWards, "change"
        ));
    }

    private void addProcuringEntitySection() {
        Select2ChoiceBootstrapFormComponent<ProcuringEntity> issuedBy = ComponentUtil.addSelect2ChoiceField(editForm,
                "issuedBy", procuringEntityService);
        //issuedBy.required();
        issuedBy.getField().add(new AjaxComponentUpdatingBehavior("change"));

        procuringEntityEmail = new GenericSleepFormComponent<>("emailAddress", (IModel<String>) () -> {
            if (issuedBy.getModelObject() != null) {
                return issuedBy.getModelObject().getEmailAddress();
            }
            return null;
        });
        procuringEntityEmail.add(new DgFmBehavior(issuedBy));
        procuringEntityEmail.setOutputMarkupId(true);
        editForm.add(procuringEntityEmail);

        procuringEntityAddress = new GenericSleepFormComponent<>("address", (IModel<String>) () -> {
            if (issuedBy.getModelObject() != null) {
                return issuedBy.getModelObject().getAddress();
            }
            return null;
        });
        procuringEntityAddress.add(new DgFmBehavior(issuedBy));
        procuringEntityAddress.setOutputMarkupId(true);
        editForm.add(procuringEntityAddress);
    }

    private IValidator<String> tenderDocOrTenderLinkRequiredValidator() {
        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new TenderDocumentValidator(id.toLong(-1));
    }

    private class AjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        AjaxComponentUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(procuringEntityEmail);
            target.add(procuringEntityAddress);
        }
    }

    public class TenderDocumentValidator implements IValidator<String>, INullAcceptingValidator<String> {
        private final Long id;

        public TenderDocumentValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String tenderLinkValue = validatable.getValue();
            final Set<FileMetadata> uploadedFiles = editForm.getModelObject().getFormDocs();

            if (StringUtils.isEmpty(tenderLinkValue) && uploadedFiles.isEmpty()) {
                final ValidationError error = new ValidationError(getString("tenderDocRequired"));
                validatable.error(error);
            }

        }
    }

    @Override
    protected Tender newInstance() {
        final Tender tender = super.newInstance();
        tender.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return tender;
    }
}
