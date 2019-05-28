package org.devgateway.toolkit.forms.wicket.page.overview;

import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.components.CompoundSectionPanel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-05-24
 */
public abstract class AbstractListViewStatus<T> extends CompoundSectionPanel<List<T>> {
    protected WebMarkupContainer listWrapper;

    private ListView<T> listView;

    private Set<Long> expandedContainerIds = new HashSet<>();

    @SpringBean
    protected SessionMetadataService sessionMetadataService;

    public AbstractListViewStatus(final String id, final IModel<List<T>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        listView = new ListView<T>("list", getModel()) {
            @Override
            protected void populateItem(final ListItem<T> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<T> compoundPropertyModel = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // add hideable container
                final TransparentWebMarkupContainer hideableContainer =
                        new TransparentWebMarkupContainer("hideableContainer") {
                            @Override
                            protected void onComponentTag(final ComponentTag tag) {
                                super.onComponentTag(tag);

                                if (isExpanded(item)) {
                                    Attributes.addClass(tag, "in");
                                } else {
                                    Attributes.removeClass(tag, "in");
                                }
                            }
                        };
                hideableContainer.setOutputMarkupId(true);
                hideableContainer.setOutputMarkupPlaceholderTag(true);
                item.add(hideableContainer);

                // add header
                final AjaxLink<Void> header = new AjaxLink<Void>("header") {
                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        if (isExpanded(item)) {
                            target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('hide')");
                            expandedContainerIds.remove(getItemId(item));
                        } else {
                            target.prependJavaScript("$('#" + hideableContainer.getMarkupId() + "').collapse('show')");
                            expandedContainerIds.add(getItemId(item));
                        }
                        target.add(this);
                    }

                    @Override
                    protected void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);

                        if (isExpanded(item)) {
                            Attributes.removeClass(tag, "collapsed");
                        } else {
                            Attributes.addClass(tag, "collapsed");
                        }
                    }
                };
                item.add(header);

                // populate header
                populateHeader("headerFragment", header, item);

                // we add the rest of the items in the listItem
                populateCompoundListItem(item);

                // populate hideable container
                populateHideableContainer("containerFragment", hideableContainer, item);
            }

        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);
    }

    public void removeListItems() {
        // remove all items to automatically rebuild all ListItems before rendering the list view
        listView.removeAll();
    }

    protected abstract void populateCompoundListItem(ListItem<T> item);

    protected abstract void populateHeader(String headerFragmentId,
                                           AjaxLink<Void> header,
                                           ListItem<T> item);

    protected abstract void populateHideableContainer(String containerFragmentId,
                                                      TransparentWebMarkupContainer hideableContainer,
                                                      ListItem<T> item);

    protected abstract Long getItemId(ListItem<T> item);

    private boolean isExpanded(final ListItem<T> item) {
        return expandedContainerIds.contains(getItemId(item));
    }
}