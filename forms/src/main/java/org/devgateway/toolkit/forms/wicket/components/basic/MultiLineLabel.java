package org.devgateway.toolkit.forms.wicket.components.basic;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class MultiLineLabel extends GenericPanel<List<String>> {

    public MultiLineLabel(String id, IModel<List<String>> model) {
        super(id, model);

        add(new ListView<String>("ul", model) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("li", item.getModel()));
            }
        });
    }
}
