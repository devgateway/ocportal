package org.devgateway.toolkit.forms.wicket.page.overview.status;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.CompoundSectionPanel;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-17
 */
public class ListViewStatusOverview extends CompoundSectionPanel<List<StatusOverviewData>> {
    private WebMarkupContainer listWrapper;

    private ListView<StatusOverviewData> listView;

    public ListViewStatusOverview(final String id, final IModel<List<StatusOverviewData>> model) {
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

        listView = new ListView<StatusOverviewData>("list", getModel()) {
            @Override
            protected void populateItem(final ListItem<StatusOverviewData> item) {
                // we wrap the item model on a compound model so we can use the field ids as property models
                final CompoundPropertyModel<StatusOverviewData> compoundPropertyModel
                        = new CompoundPropertyModel<>(item.getModel());

                // we set back the model as the compound model, thus ensures the rest of the items added will benefit
                item.setModel(compoundPropertyModel);

                // add the items in the listItem
                item.add(new Label("test", "aaaa"));
            }
        };

        listView.setReuseItems(true);
        listView.setOutputMarkupId(true);
        listWrapper.add(listView);
    }
}
