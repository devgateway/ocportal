package org.devgateway.toolkit.forms.wicket.page.lists.feedback;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.feedback.EditFeedbackMessagePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListFeedbackMessagePage extends AbstractListPage<ReplyableFeedbackMessage> {

    @SpringBean
    private ReplyableFeedbackMessageService repository;

    public ListFeedbackMessagePage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = repository;
        this.editPageClass = EditFeedbackMessagePage.class;
    }

    @Override
    protected void onInitialize() {
        hasNewPage = false;
        columns.add(new PropertyColumn<>(new Model<>("Page URL"), "url", "url"));
        columns.add(new PropertyColumn<>(new Model<>("Visible"), "visible", "visible"));
        columns.add(new PropertyColumn<>(new Model<>("Name"), "name", "name"));
        columns.add(new PropertyColumn<>(new Model<>("Date Created"), "createdDate", "createdDate.get"));
        columns.add(new PropertyColumn<>(new Model<>("Date Modified"), "lastModifiedDate", "lastModifiedDate.get"));
        columns.add(new PropertyColumn<>(new Model<>("Replies"), null, "replies.size"));
        super.onInitialize();

    }
}
