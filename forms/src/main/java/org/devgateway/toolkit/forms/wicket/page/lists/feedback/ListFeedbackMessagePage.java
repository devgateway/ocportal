package org.devgateway.toolkit.forms.wicket.page.lists.feedback;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.feedback.EditFeedbackMessagePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.FeedbackMessageService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListFeedbackMessagePage extends AbstractListPage<FeedbackMessage> {

    @SpringBean
    private FeedbackMessageService alertService;

    public ListFeedbackMessagePage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = alertService;
        this.editPageClass = EditFeedbackMessagePage.class;
    }

    @Override
    protected void onInitialize() {
        columns.add(new PropertyColumn<>(new Model<>("Email"), "email", "email"));
        columns.add(new PropertyColumn<>(new Model<>("Name"), "name", "name"));
        super.onInitialize();

    }
}
