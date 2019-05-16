package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.ProfessionalOpinionFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProfessionalOpinionPage;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-24
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/professionalOpinions")
public class ListProfessionalOpinionPage extends ListAbstractMakueniEntityPage<ProfessionalOpinion> {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    public ListProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = professionalOpinionService;
        this.editPageClass = EditProfessionalOpinionPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


    @Override
    public JpaFilterState<ProfessionalOpinion> newFilterState() {
        return new ProfessionalOpinionFilterState();
    }
}
