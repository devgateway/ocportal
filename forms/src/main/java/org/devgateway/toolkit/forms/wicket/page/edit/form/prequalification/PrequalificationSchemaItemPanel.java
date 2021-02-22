package org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.devgateway.toolkit.forms.wicket.components.ListViewSectionPanel;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationSchemaItemsValidator;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationSchemaValidator;

import java.util.List;

public class PrequalificationSchemaItemPanel extends ListViewSectionPanel<PrequalificationSchemaItem,
        PrequalificationSchema> {

    @SpringBean
    private TargetGroupService targetGroupService;

    public PrequalificationSchemaItemPanel(final String id) {
        super(id, false);
    }

    @Override
    public PrequalificationSchemaItem createNewChild(final IModel<PrequalificationSchema> parentModel) {
        final PrequalificationSchemaItem child = new PrequalificationSchemaItem();
        child.setParent(parentModel.getObject());
        child.setExpanded(true);
        child.setEditable(true);
        return child;
    }

    protected List<GenericBootstrapFormComponent<?, ?>> getItemCodeComponents() {
        return getChildComponentsByName("code");
    }



    @Override
    protected Label createShowDetailsLink() {
        Label showDetailsLink = super.createShowDetailsLink();
        showDetailsLink.setVisibilityAllowed(false);
        return showDetailsLink;
    }

    protected boolean getWrongDistinctItemNameCount() {
       return UniquePrequalificationSchemaItemsValidator.wrongDistinctItemCount(
               PrequalificationSchemaItemPanel.this.getModelObject(),
                PrequalificationSchemaItem::getName);
    }

    protected boolean getWrongDistinctItemCodeCount() {
        return UniquePrequalificationSchemaItemsValidator.wrongDistinctItemCount(
                PrequalificationSchemaItemPanel.this.getModelObject(),
                PrequalificationSchemaItem::getCode);
    }


    protected class PrequalifcationSchemaItemPanelValidator implements IFormValidator {

        private String childComponentName;
        private SerializableSupplier<Boolean> nonUniqueItemsSupplier;
        private String errorKey;

        public PrequalifcationSchemaItemPanelValidator(String childComponentName,
                                                       SerializableSupplier<Boolean> nonUniqueItemsSupplier,
                                                       String errorKey) {
            this.errorKey = errorKey;
            this.nonUniqueItemsSupplier = nonUniqueItemsSupplier;
            this.childComponentName = childComponentName;
        }

        protected List<GenericBootstrapFormComponent<?, ?>> getItemNameComponents() {
            return getChildComponentsByName(childComponentName);
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return ComponentUtil.getFormComponentsFromBootstrapComponents(getItemNameComponents());
        }

        @Override
        public void validate(Form<?> form) {
            if (nonUniqueItemsSupplier.get()) {
                form.error(getString(errorKey));
                addErrorAndRefreshComponents(getItemNameComponents(), errorKey);
            }
        }


    }


    @Override
    public void populateCompoundListItem(final ListItem<PrequalificationSchemaItem> item) {
       ComponentUtil.addTextField(item, "code").required();
       ComponentUtil.addTextField(item, "name").required();
       ComponentUtil.addSelect2MultiChoiceField(item, "companyCategories", targetGroupService).required();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form form = findParent(Form.class);
        form.add(new PrequalifcationSchemaItemPanelValidator("name",
                this::getWrongDistinctItemNameCount, "wrongDistinctItemNameCount"));
        form.add(new PrequalifcationSchemaItemPanelValidator("code",
                this::getWrongDistinctItemCodeCount, "wrongDistinctItemCodeCount"));
    }

    @Override
    protected boolean filterListItem(PrequalificationSchemaItem prequalificationSchemaItem) {
        return true;
    }


}