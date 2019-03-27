package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapAddButton;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapDeleteButton;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;

import java.util.List;

/**
 * @param <T>      The current list data type
 * @param <PARENT> The parent field data type
 * @author idobre
 * @since 10/5/16
 * <p>
 * Class that displays a list of T type with the possibility of adding/removing elements.
 */

public abstract class ListViewSectionPanel<T extends AbstractAuditableEntity, PARENT extends AbstractAuditableEntity>
        extends CompoundSectionPanel<List<T>> {
    private WebMarkupContainer listWrapper;

    private ListView<T> listView;

    private static final String ID_ACCORDION = "accordion";

    private static final String ID_HIDEABLE_CONTAINER = "hideableContainer";

    private static final String ID_ACCORDION_TOGGLE = "accordionToggle";

    public ListViewSectionPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        listWrapper.add(new Label("panelTitle", title));

        listWrapper.add(new Label("totalEntries",
                (IModel<Integer>) () -> ListViewSectionPanel.this.getModel().getObject().size()));

        final AjaxLink<Void> showHideAllEntries = new AjaxLink<Void>("showHideAllEntries") {
            @Override
            public void onClick(AjaxRequestTarget target) {

            }
        };
        listWrapper.add(showHideAllEntries);

        final IModel listModel = new FilteredListModel<T>(getModel()) {
            @Override
            protected boolean accept(T t) {
                return filterListItem(t);
            }
        };

        listView = new ListView<T>("list", listModel) {
            @Override
            protected void populateItem(final ListItem<T> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<T> compoundPropertyModel = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // we add the rest of the items in the listItem
                populateCompoundListItem(item);


                final TransparentWebMarkupContainer accordion = new TransparentWebMarkupContainer(ID_ACCORDION);
                accordion.setOutputMarkupId(true);
                item.add(accordion);

                final AjaxLink<Void> accordionToggle = new AjaxLink<Void>(ID_ACCORDION_TOGGLE) {
                    @Override
                    public void onClick(final AjaxRequestTarget target) {

                    }
                };
                accordionToggle.setOutputMarkupId(true);
                accordion.add(accordionToggle);

                // we add the special header field
                // accordionToggle.add(getHeaderField("headerField", new CompoundPropertyModel<>(item.getModel())));
                accordionToggle.add(new Label("headerField", "Header Field"));

                final Label showDetailsLink = new Label("showDetailsLink", new ResourceModel("showDetailsLink"));
                accordionToggle.add(showDetailsLink);

                // the section that will collapse
                final TransparentWebMarkupContainer hideableContainer = new TransparentWebMarkupContainer(ID_HIDEABLE_CONTAINER);
                hideableContainer.setOutputMarkupId(true);
                hideableContainer.setOutputMarkupPlaceholderTag(true);
                accordion.add(hideableContainer);

                // we add the remove button
                final BootstrapDeleteButton removeButton = getRemoveChildButton(item.getModelObject());
                hideableContainer.add(removeButton);
            }
        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);

        final BootstrapAddButton addButton = getAddNewChildButton();
        add(addButton);
    }

    /**
     * Removes a child based on its index
     *
     * @param item
     * @return
     */
    private BootstrapDeleteButton getRemoveChildButton(final T item) {
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
    final BootstrapAddButton getAddNewChildButton() {
        final BootstrapAddButton newButton = new BootstrapAddButton("newButton", new ResourceModel("newButton")) {
            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                final T newChild = createNewChild(
                        (IModel<PARENT>) ListViewSectionPanel.this.getParent().getDefaultModel());
                ListViewSectionPanel.this.getModel().getObject().add(newChild);

                listView.removeAll();
                target.add(listWrapper);
            }

        };

        newButton.setOutputMarkupPlaceholderTag(true);
        return newButton;
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
