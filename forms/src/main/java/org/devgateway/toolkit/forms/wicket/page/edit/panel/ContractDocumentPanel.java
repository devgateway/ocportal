package org.devgateway.toolkit.forms.wicket.page.edit.panel;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.devgateway.toolkit.persistence.dao.form.Contract;

import org.devgateway.toolkit.persistence.dao.form.ContractDocument;
import org.devgateway.toolkit.persistence.service.category.ContractDocumentTypeService;

public class ContractDocumentPanel extends ListViewSectionPanel<ContractDocument, Contract> {

    @SpringBean
    private ContractDocumentTypeService contractDocumentTypeService;

    public ContractDocumentPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public ContractDocument createNewChild(final IModel<Contract> parentModel) {
        final ContractDocument child = new ContractDocument();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);

        return child;
    }

    @Override
    public void populateCompoundListItem(final ListItem<ContractDocument> item) {
        Select2ChoiceBootstrapFormComponent<ContractDocumentType> contractDocumentType = ComponentUtil
                .addSelect2ChoiceField(item, "contractDocumentType", contractDocumentTypeService);
        contractDocumentType.required();
        item.add(contractDocumentType);

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.maxFiles(1);
        formDocs.required();
        item.add(formDocs);
    }

    @Override
    protected boolean filterListItem(final ContractDocument contractDocuments) {
        return true;
    }
    
   
}