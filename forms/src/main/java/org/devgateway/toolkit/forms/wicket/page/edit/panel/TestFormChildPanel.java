package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.dao.TestFormChild;

/**
 * @author idobre
 * @since 2019-03-22
 */
public class TestFormChildPanel extends ListViewSectionPanel<TestFormChild, TestForm> {

    public TestFormChildPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public TestFormChild createNewChild(final IModel<TestForm> parentModel) {
        final TestFormChild child = new TestFormChild();
        child.setTestForm(parentModel.getObject());

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<TestFormChild> item) {

    }
}
