package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class RevisionsPanel<TYPE> extends GenericPanel<List<TYPE>> {

    private final String auditProperty;
    protected TransparentWebMarkupContainer revisionsCollapse;
    protected TransparentWebMarkupContainer revisionsMasterGroup;
    protected TransparentWebMarkupContainer revisionsChildGroup;
    protected WebMarkupContainer revisionsPanelLink;
    protected Label revisionsPanelLabel;


    public RevisionsPanel(final String id, final IModel<List<TYPE>> model, final String auditProperty) {
        super(id, model);
        this.auditProperty = auditProperty;
    }

    private IModel<String> getLabelKeyFromGenericComponent() {
        return ((GenericBootstrapFormComponent) this.getParent()).getLabelModel();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        revisionsCollapse = new TransparentWebMarkupContainer("revisionsCollapse");
        revisionsCollapse.setOutputMarkupId(true);
        add(revisionsCollapse);
        setOutputMarkupId(true);
        revisionsPanelLink = new WebMarkupContainer("revisionsPanelLink");
        revisionsPanelLabel = new Label("revisionsPanelLabel", getLabelKeyFromGenericComponent());
        revisionsPanelLink.add(revisionsPanelLabel);
        revisionsPanelLink.add(AttributeModifier.append("href", "#" + revisionsCollapse.getMarkupId()));

        add(revisionsPanelLink);
        add(new ListView<TYPE>("rows", getModel()) {
            public void populateItem(final ListItem<TYPE> item) {
                Object[] obj = (Object[]) item.getModelObject();
                Label data = new Label("data", new PropertyModel<>(
                        obj[0],
                        auditProperty
                ));
                data.setEscapeModelStrings(false);
                item.add(data);

                Label lastUpdated = new Label("lastUpdated", new PropertyModel<>(
                        obj[0],
                        "lastUpdated.toDate"
                ));
                item.add(lastUpdated);


                Label lastModifiedBy = new Label("lastModifiedBy", new PropertyModel<>(
                        obj[0],
                        "lastModifiedBy"
                ));
                item.add(lastModifiedBy);

                Label revisionType = new Label("revisionType", new PropertyModel<>(
                        obj[2],
                        "name"
                ));
                item.add(revisionType);

                Label id = new Label("id", item.getIndex());
                item.add(id);
            }
        });

    }
}
