package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ProfessionalOpinionItemPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-24
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditProfessionalOpinionPage extends EditAbstractTenderProcessClientEntityPage<ProfessionalOpinion>
        implements ProcurementRoleAssignable {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    public EditProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = professionalOpinionService;
    }

    public EditProfessionalOpinionPage() {
        this(new PageParameters());
    }


    @Override
    protected void onInitialize() {
        editForm.attachFm("professionalOpinionForm");
        super.onInitialize();

        Fragment extraFields = new Fragment("extraReadOnlyFields", "extraReadOnlyFields", this);
        IModel<Tender> tenderModel = editForm.getModel()
                .map(AbstractTenderProcessClientEntity::getTenderProcess)
                .map(TenderProcess::getSingleTender);
        extraFields.add(new GenericSleepFormComponent<>("tenderNumber",
                tenderModel.map(Tender::getTenderNumber)));
        extraFields.add(new GenericSleepFormComponent<>("tenderTitle",
                tenderModel.map(Tender::getTenderTitle)));
        editForm.replace(extraFields);

        ProfessionalOpinionItemPanel items = new ProfessionalOpinionItemPanel("items");
        editForm.add(items);
    }

    @Override
    protected ProfessionalOpinion newInstance() {
        final ProfessionalOpinion professionalOpinion = super.newInstance();
        professionalOpinion.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return professionalOpinion;
    }
}
