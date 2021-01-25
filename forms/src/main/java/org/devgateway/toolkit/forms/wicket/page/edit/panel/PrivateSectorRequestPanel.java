package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.forms.validators.AfterThanDateValidator;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.devgateway.toolkit.persistence.dao.form.PrivateSectorRequest;

import java.util.Date;

public class PrivateSectorRequestPanel extends ListViewSectionPanel<PrivateSectorRequest, InspectionReport> {

    public PrivateSectorRequestPanel(final String id) {
        super(id);
    }

    @Override
    public PrivateSectorRequest createNewChild(final IModel<InspectionReport> parentModel) {
        final PrivateSectorRequest child = new PrivateSectorRequest();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<PrivateSectorRequest> item) {
        FileInputBootstrapFormComponent upload = new FileInputBootstrapFormComponent("upload");
        upload.maxFiles(1);
        item.add(upload);

        GenericBootstrapFormComponent<Date, TextField<Date>> requestDate =
                ComponentUtil.addDateField(item, "requestDate").required();
        requestDate.getField().add(new AfterThanDateValidator(item.getModel().map(PrivateSectorRequest::getParent)
                .map(InspectionReport::getApprovedDate)));
    }

    @Override
    protected boolean filterListItem(PrivateSectorRequest privateSectorRequest) {
        return true;
    }

}
