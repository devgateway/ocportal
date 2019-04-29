package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.AwardNotificationFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAwardNotificationPage;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/awardNotifications")
public class ListAwardNotificationPage extends ListAbstractMakueniFormPage<AwardNotification> {
    @SpringBean
    protected AwardNotificationService professionalOpinionService;

    public ListAwardNotificationPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = professionalOpinionService;
        this.editPageClass = EditAwardNotificationPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


    @Override
    public JpaFilterState<AwardNotification> newFilterState() {
        return new AwardNotificationFilterState();
    }
}
