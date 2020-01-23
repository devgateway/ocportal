package org.devgateway.toolkit.forms.wicket.page.lists.feedback;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.feedback.EditFeedbackMessagePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.feedback.ReplyableFeedbackMessageService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.feedback.ReplyableFeedbackMessageFilterState;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author mpostelnicu
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListFeedbackMessagePage extends AbstractListPage<ReplyableFeedbackMessage> {

    @SpringBean
    private ReplyableFeedbackMessageService repository;

    @SpringBean
    protected DepartmentService departmentService;

    protected final List<Department> departments;

    public class FeedbackActionPanel extends ActionPanel {

        /**
         * @param id
         * @param model
         */
        public FeedbackActionPanel(String id, IModel<ReplyableFeedbackMessage> model) {
            super(id, model);

            ReplyableFeedbackMessage entity = (ReplyableFeedbackMessage) this.getDefaultModelObject();

            BootstrapExternalLink viewLink = new BootstrapExternalLink("view",
                    Model.of("ui/index.html#!/" + entity.getUrl()), Buttons.Type.Warning
            ) {
            };
            viewLink.setLabel(new StringResourceModel("view", ListFeedbackMessagePage.this, null));
            viewLink.setIconType(FontAwesomeIconType.eye).setSize(Buttons.Size.Small);
            add(viewLink);

        }
    }


    public ListFeedbackMessagePage(final PageParameters parameters) {
        super(parameters);

        this.departments = departmentService.findAll();
        this.jpaService = repository;
        this.editPageClass = EditFeedbackMessagePage.class;
    }

    @Override
    protected void onInitialize() {
        hasNewPage = false;
        columns.add(new PropertyColumn<>(new Model<>("Page URL"), "url", "url"));
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "department", "department", new ListModel(departments), dataTable
        ));
        columns.add(new PropertyColumn<>(new Model<>("Visible"), "visible", "visible"));
        columns.add(new PropertyColumn<>(new Model<>("Contributor Name"), "name", "name"));
        columns.add(new PropertyColumn<>(new Model<>("Date Created"), "createdDate", "createdDate.get"));
        columns.add(new PropertyColumn<>(new Model<>("Date Modified"), "lastModifiedDate", "lastModifiedDate.get"));
        columns.add(new PropertyColumn<>(new Model<>("Replies"), null, "replies.size"));
        super.onInitialize();

    }

    @Override
    public JpaFilterState<ReplyableFeedbackMessage> newFilterState() {
        return new ReplyableFeedbackMessageFilterState();
    }

    @Override
    public ActionPanel getActionPanel(String id, IModel<ReplyableFeedbackMessage> model) {
        return new FeedbackActionPanel(id, model);
    }
}
