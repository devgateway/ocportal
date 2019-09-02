package org.devgateway.toolkit.forms.wicket.components;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.devgateway.toolkit.forms.util.JQueryUtil;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @param <T>      The current list data type
 * @param <PARENT> The parent field data type
 * @author idobre
 * @since 10/5/16
 * <p>
 * Class that displays a list of T type with the possibility of adding/removing elements.
 */

public abstract class ListViewSectionPanel<T extends AbstractAuditableEntity & ListViewItem,
        PARENT extends AbstractAuditableEntity> extends CompoundSectionPanel<List<T>> {
    private static final Logger logger = LoggerFactory.getLogger(ListViewSectionPanel.class);

    public static final String ID_ACCORDION = "accordion";

    public static final String ID_HIDEABLE_CONTAINER = "hideableContainer";

    public static final String ID_ACCORDION_TOGGLE = "accordionToggle";

    protected WebMarkupContainer listWrapper;

    protected ListView<T> listView;

    protected NotificationPanel addButtonNotificationPanel;

    public ListViewSectionPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        addFilterPanel();

        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        listWrapper.add(new Label("panelTitle", title));

        listWrapper.add(new Label("totalEntries",
                (IModel<Integer>) () -> ListViewSectionPanel.this.getModel().getObject().size()));

        final AjaxLink<Void> showHideAllEntries = new AjaxLink<Void>("showHideAllEntries") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                // take the list of elements from the path: listWrapper/list
                final ListView<T> list = (ListView<T>) ListViewSectionPanel.this.get("listWrapper").get("list");

                if (list != null) {
                    // determine if we need to show or hide all the elements
                    final Boolean show = list.size() != list.getModelObject()
                            .parallelStream()
                            .filter(item -> item.getExpanded()).count();

                    for (int i = 0; i < list.size(); i++) {
                        final TransparentWebMarkupContainer accordion =
                                (TransparentWebMarkupContainer) list.get("" + i).get(ID_ACCORDION);

                        if (accordion != null) {
                            final Label showDetailsLink =
                                    (Label) accordion.get(ID_ACCORDION_TOGGLE).get("showDetailsLink");

                            if (show) {
                                showSection(list.getModelObject().get(i), target,
                                        accordion.get(ID_HIDEABLE_CONTAINER), showDetailsLink);
                            } else {
                                hideSection(list.getModelObject().get(i), target,
                                        accordion.get(ID_HIDEABLE_CONTAINER), showDetailsLink);
                            }
                        }
                    }
                }
            }
        };
        listWrapper.add(showHideAllEntries);

        final IModel listModel = new FilteredListModel<T>(getModel()) {
            @Override
            protected boolean accept(final T t) {
                return filterListItem(t);
            }
        };

        listView = new ListView<T>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<T> item) {
                item.setOutputMarkupId(true);
                item.setOutputMarkupPlaceholderTag(true);

                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<T> compoundPropertyModel = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // we add the header #
                item.add(new Label("headerNo", 1 + item.getIndex()));

                // just keep the last element editable and expanded
                if (item.getIndex() == getModel().getObject().size() - 1) {
                    item.getModelObject().setExpanded(true);
                    item.getModelObject().setEditable(true);
                }

                // we add the rest of the items in the listItem
                populateCompoundListItem(item);

                addAcordion(item);
            }
        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);

        final BootstrapAddButton addButton = getAddNewChildButton();
        add(addButton);

        addButtonNotificationPanel = new NotificationPanel("addButtonNotificationPanel");
        addButtonNotificationPanel.setOutputMarkupId(true);
        addButtonNotificationPanel.setFilter(new ComponentFeedbackMessageFilter(addButton));
        add(addButtonNotificationPanel);
    }

    private void addAcordion(final ListItem<T> item) {
        final Label showDetailsLink = new Label("showDetailsLink", new ResourceModel("showDetailsLink"));
        showDetailsLink.setOutputMarkupId(true);

        // the section that will collapse
        final TransparentWebMarkupContainer hideableContainer =
                new TransparentWebMarkupContainer(ID_HIDEABLE_CONTAINER);
        hideableContainer.setOutputMarkupId(true);
        hideableContainer.setOutputMarkupPlaceholderTag(true);

        final TransparentWebMarkupContainer accordion = new TransparentWebMarkupContainer(ID_ACCORDION);
        accordion.setOutputMarkupId(true);
        item.add(accordion);

        final AjaxLink<Void> accordionToggle = new AjaxLink<Void>(ID_ACCORDION_TOGGLE) {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                final T modelObject = item.getModelObject();
                if (modelObject.getExpanded()) {
                    hideSection(modelObject, target, hideableContainer, showDetailsLink);
                } else {
                    showSection(modelObject, target, hideableContainer, showDetailsLink);
                }
            }
        };
        accordionToggle.setOutputMarkupId(true);
        accordion.add(accordionToggle);

        // we add the special header field
        accordionToggle.add(getHeaderField("headerField", new CompoundPropertyModel<>(item.getModel())));

        accordionToggle.add(showDetailsLink);
        accordion.add(hideableContainer);

        final NotificationPanel removeButtonNotificationPanel = new NotificationPanel("removeButtonNotificationPanel");
        removeButtonNotificationPanel.setOutputMarkupId(true);
        hideableContainer.add(removeButtonNotificationPanel);

        // we add the remove button
        final BootstrapDeleteButton removeButton = getRemoveChildButton(item.getModelObject(),
                removeButtonNotificationPanel);
        hideableContainer.add(removeButton);
        removeButtonNotificationPanel.setFilter(new ComponentFeedbackMessageFilter(removeButton));

        // we add the edit button
        final LaddaAjaxButton editButton = getEditChildButton(item);
        editButton.setVisibilityAllowed(item.getModelObject().getEditable() != null
                && !item.getModelObject().getEditable());
        hideableContainer.add(editButton);

        // if we display a new element that was just added then we make the accordion enabled
        if (item.getModelObject().isNew()
                && item.getIndex() == getModel().getObject().size() - 1) {
            item.getModelObject().setExpanded(true);

            // jump to top of the new item
            final Optional<AjaxRequestTarget> target = RequestCycle.get().find(AjaxRequestTarget.class);
            if (target.isPresent()) {
                goToComponent(target.get(), accordion.getMarkupId());
            }
        }

        if (item.getModelObject().getExpanded()) {
            hideableContainer.add(new AttributeModifier("class", new Model<>("panel-body panel-collapse collapse in")));
            showDetailsLink.setDefaultModel(new ResourceModel("hideDetailsLink"));
        }
    }

    /**
     * Open the accordion.
     */
    public void showSection(final ListViewItem item, final AjaxRequestTarget target,
                            final Component hideableContainer, final Label showDetailsLink) {
        target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('show')");
        item.setExpanded(true);

        showDetailsLink.setDefaultModel(new ResourceModel("hideDetailsLink"));
        target.add(showDetailsLink);
    }

    /**
     * Close the accordion.
     */
    public void hideSection(final ListViewItem item, final AjaxRequestTarget target,
                            final Component hideableContainer, final Label showDetailsLink) {
        target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('hide')");
        item.setExpanded(false);

        showDetailsLink.setDefaultModel(new ResourceModel("showDetailsLink"));
        target.add(showDetailsLink);
    }

    /**
     * Removes a child based on its index
     *
     * @param item
     * @return
     */
    private LaddaAjaxButton getEditChildButton(final ListItem<T> item) {
        final LaddaAjaxButton editButton = new LaddaAjaxButton("edit",
                new ResourceModel("editButton"),
                Buttons.Type.Info) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                final T modelObject = item.getModelObject();
                modelObject.setEditable(true);

                listView.removeAll();
                target.add(listWrapper);
            }

            @Override
            public void onEvent(final IEvent<?> event) {
                ComponentUtil.enableDisableEvent(this, event);
            }

            @Override
            protected void onInitialize() {
                super.onInitialize();

                if (ComponentUtil.isPrintMode()) {
                    setVisibilityAllowed(false);
                }
            }
        };
        editButton.setDefaultFormProcessing(false);
        editButton.setIconType(FontAwesomeIconType.edit);
        editButton.setOutputMarkupPlaceholderTag(true);
        return editButton;
    }

    /**
     * Removes a child based on its index
     *
     * @param item
     * @return
     */
    protected BootstrapDeleteButton getRemoveChildButton(final T item
            , final NotificationPanel removeButtonNotificationPanel) {
        final BootstrapDeleteButton removeButton = new BootstrapDeleteButton("remove",
                new ResourceModel("removeButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                ListViewSectionPanel.this.getModelObject().remove(item);
                listView.removeAll();
                target.add(listWrapper);
            }
        };

        removeButton.setOutputMarkupPlaceholderTag(true);
        return removeButton;
    }

    /**
     * Returns the new child button.
     */
    protected BootstrapAddButton getAddNewChildButton() {
        return new AddNewChildButton("newButton", new ResourceModel("newButton"));
    }

    public class AddNewChildButton extends BootstrapAddButton {
        public AddNewChildButton(final String id, final IModel<String> model) {
            super(id, model);
        }

        @Override
        protected void onSubmit(final AjaxRequestTarget target) {
            final T newChild = createNewChild(
                    (IModel<PARENT>) ListViewSectionPanel.this.getParent().getDefaultModel());
            ListViewSectionPanel.this.getModelObject().add(newChild);

            listView.removeAll();
            target.add(listWrapper);
        }
    }

    private static void goToComponent(final AjaxRequestTarget target, final String markupId) {
        target.appendJavaScript(JQueryUtil.animateScrollTop("#" + markupId, 500));
    }

    /**
     * Override this function if you need to add a filter form for the list view component.
     */
    protected void addFilterPanel() {
        final TransparentWebMarkupContainer hiddenForm = new TransparentWebMarkupContainer("listFilterPanel");
        hiddenForm.setVisibilityAllowed(false);
        add(hiddenForm);
    }

    protected Component getHeaderField(final String id, final CompoundPropertyModel<T> compoundModel) {
        return new TransparentWebMarkupContainer(id);
    }


    /**
     * Use the constructor for new children and return the entity after setting its parent.
     *
     * @param parentModel the model of the parent
     */
    public abstract T createNewChild(IModel<PARENT> parentModel);

    /**
     * Populates the list item elements.
     *
     * @param item
     */
    public abstract void populateCompoundListItem(ListItem<T> item);

    protected abstract boolean filterListItem(T t);
}
