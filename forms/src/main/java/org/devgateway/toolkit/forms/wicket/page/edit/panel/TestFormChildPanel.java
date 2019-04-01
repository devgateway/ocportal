package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.dao.TestFormChild;

import java.io.Serializable;

/**
 * @author idobre
 * @since 2019-03-22
 */
public class TestFormChildPanel extends ListViewSectionPanel<TestFormChild, TestForm> {
    private final ListFilterBean listFilterBean;

    public TestFormChildPanel(final String id) {
        super(id);

        listFilterBean = new ListFilterBean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    protected void addFilterForm() {
        final ListFilterForm listFilterForm = new ListFilterForm(
                "listFilterForm", new CompoundPropertyModel<>(listFilterBean));
        add(listFilterForm);
    }

    @Override
    public TestFormChild createNewChild(final IModel<TestForm> parentModel) {
        final TestFormChild child = new TestFormChild();
        child.setTestForm(parentModel.getObject());

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<TestFormChild> item) {
        final TextFieldBootstrapFormComponent<String> header = ComponentUtil.addTextField(item, "header", false);
        header.required();

        final TextFieldBootstrapFormComponent<Integer> value = ComponentUtil.addIntegerTextField(item, "value", false);
        value.required();
    }

    @Override
    protected boolean filterListItem(final TestFormChild testFormChild) {
        // don't filter unsaved entities
        if (testFormChild.getId() == null) {
            return true;
        } else {
            if (listFilterBean.getHeader() != null) {
                // it was saved as null when it was saved as draft
                if (testFormChild.getHeader() == null) {
                    return false;
                }
                return testFormChild.getHeader().toLowerCase()
                        .matches(".*" + listFilterBean.getHeader().toLowerCase() + ".*");
            }

            return true;
        }
    }

    class ListFilterForm extends BootstrapForm<ListFilterBean> {
        private TextFieldBootstrapFormComponent<String> header;

        ListFilterForm(final String componentId, final IModel<ListFilterBean> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            header = ComponentUtil.addTextLoginField(this, "header", false);

            final LaddaAjaxButton submit = new LaddaAjaxButton("submit",
                    new Model<>("Filter"),
                    Buttons.Type.Info) {
                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    // automatically rebuild all ListItems before rendering the list view
                    listView.removeAll();
                    target.add(listWrapper);
                }
            };
            submit.setIconType(FontAwesomeIconType.search);
            add(submit);
        }
    }

    class ListFilterBean implements Serializable {
        private String header;

        public String getHeader() {
            return header;
        }

        public void setHeader(final String header) {
            this.header = header;
        }
    }
}
