package org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.TextContentModal;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxLink;
import nl.dries.wicket.hibernate.dozer.DozerModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.AjaxDownloadBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification.EditPrequalifiedSupplierPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractBaseListPage;
import org.devgateway.toolkit.forms.wicket.providers.AbstractDataProvider;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.forms.wicket.providers.PrequalificationSchemaItemChoiceProvider;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaServiceDataProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.Supplier_;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem_;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier_;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierExporter;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierItemService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.devgateway.toolkit.web.Constants;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.data.jpa.domain.Specification;
import org.wicketstuff.annotation.mount.MountPath;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/prequalifiedSuppliers")
public class ListPrequalifiedSupplierPage extends AbstractBaseListPage<PrequalifiedSupplierItem> {

    @SpringBean
    private PrequalifiedSupplierService prequalifiedSupplierService;

    @SpringBean
    private PrequalifiedSupplierItemService prequalifiedSupplierItemService;

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @SpringBean
    private SupplierService supplierService;

    @SpringBean
    private TargetGroupService targetGroupService;

    @SpringBean
    private SubcountyService subcountyService;

    @SpringBean
    private WardService wardService;

    private final IModel<Filter> filterModel;

    private TextContentModal deleteModal;

    private IModel<PrequalifiedSupplierItem> itemToDeleteModel;

    private IModel<Boolean> submittedSchemaModel;

    public ListPrequalifiedSupplierPage(PageParameters parameters) {
        super(parameters);

        filterModel = new DozerModel<>(new Filter());
    }

    @Override
    protected void onInitialize() {
        attachFm("prequalifiedSupplierList");

        Filter filter = filterModel.getObject();
        filter.setYearRange(prequalificationYearRangeService.findDefault());

        submittedSchemaModel = Model.of(filter.getYearRange() != null
                && filter.getYearRange().getSchema().getStatus().equals(DBConstants.Status.SUBMITTED));

        super.onInitialize();

        WebMarkupContainer draftSchemaWarning = new WebMarkupContainer("draftSchemaWarning") {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisibilityAllowed(!submittedSchemaModel.getObject());
            }
        };
        draftSchemaWarning.setOutputMarkupPlaceholderTag(true);
        add(draftSchemaWarning);

        Form<Filter> filterForm = new Form<>("form", new CompoundPropertyModel<>(filterModel));

        Select2MultiChoiceBootstrapFormComponent<PrequalificationSchemaItem> items;
        items = new Select2MultiChoiceBootstrapFormComponent<>("items",
                new PrequalificationSchemaItemChoiceProvider(filterModel.map(Filter::getYearRange)));
        filterForm.add(items);

        Select2ChoiceBootstrapFormComponent<PrequalificationYearRange> yearRange;
        yearRange = new Select2ChoiceBootstrapFormComponent<PrequalificationYearRange>("yearRange",
                new GenericPersistableJpaTextChoiceProvider<>(prequalificationYearRangeService)) {

            @Override
            protected void onInitialize() {
                super.onInitialize();
                field.getSettings().setAllowClear(false);
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                filterModel.getObject().getItems().clear();
                target.add(items);
            }
        };
        filterForm.add(yearRange);

        filterForm.add(new Select2MultiChoiceBootstrapFormComponent<>("suppliers",
                new GenericPersistableJpaTextChoiceProvider<>(supplierService)));

        filterForm.add(new Select2MultiChoiceBootstrapFormComponent<>("targetGroups",
                new GenericPersistableJpaTextChoiceProvider<>(targetGroupService)));

        filterForm.add(new Select2MultiChoiceBootstrapFormComponent<>("subcounties",
                new GenericPersistableJpaTextChoiceProvider<>(subcountyService)));

        filterForm.add(new Select2MultiChoiceBootstrapFormComponent<>("wards",
                new GenericPersistableJpaTextChoiceProvider<>(wardService)));

        filterForm.add(new BootstrapSubmitButton("submit", new ResourceModel("submit")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                getDataTable().setCurrentPage(0);

                Long id = filterForm.getModelObject().getYearRange().getId();
                PrequalificationYearRange yearRange = prequalificationYearRangeService.findById(id).orElse(null);
                submittedSchemaModel.setObject(yearRange.getSchema().getStatus().equals(DBConstants.Status.SUBMITTED));

                target.add(getDataTable(), getTopAddButton(), getBottomAddButton(), draftSchemaWarning);
            }
        });

        add(filterForm);

        deleteModal = createDeleteModal();
        add(deleteModal);

        AjaxDownloadBehavior excelExportBehavior = new AjaxDownloadBehavior(new AbstractResource() {
            @Override
            protected ResourceResponse newResourceResponse(Attributes attributes) {

                PrequalifiedSupplierExporter exporter = new PrequalifiedSupplierExporter();

                long size = getDataProvider().size();
                Iterator<? extends PrequalifiedSupplierItem> iterator = getDataProvider().iterator(0, size);
                List<PrequalifiedSupplierItem> items = new ArrayList<>();
                while (iterator.hasNext()) {
                    items.add(iterator.next());
                }

                XSSFWorkbook workbook = exporter.export(items);

                ResourceResponse response = new ResourceResponse();
                response.setContentType(Constants.ContentType.XLSX);
                response.setFileName("Prequalified Suppliers.xlsx");
                response.setWriteCallback(new WriteCallback() {
                    @Override
                    public void writeData(Attributes attributes) throws IOException {
                        workbook.write(attributes.getResponse().getOutputStream());
                    }
                });

                return response;
            }
        });
        filterForm.add(excelExportBehavior);

        LaddaAjaxLink<Void> exportLink = new LaddaAjaxLink<Void>("excelExport", Buttons.Type.Warning) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                excelExportBehavior.initiate(target);
            }
        };
        exportLink.setLabel(new StringResourceModel("excelExport.label", this));
        exportLink.setIconType(FontAwesomeIconType.file_excel_o);
        filterForm.add(exportLink);
    }

    protected AbstractDataProvider<PrequalifiedSupplierItem> createDataProvider() {
        return new SortableJpaServiceDataProvider<>(prequalifiedSupplierItemService);
    }

    private static final class Filter extends JpaFilterState<PrequalifiedSupplierItem> {

        private PrequalificationYearRange yearRange;

        private List<PrequalificationSchemaItem> items = new ArrayList<>();

        private List<Supplier> suppliers = new ArrayList<>();

        private List<TargetGroup> targetGroups = new ArrayList<>();

        private List<Subcounty> subcounties = new ArrayList<>();

        private List<Ward> wards = new ArrayList<>();

        public PrequalificationYearRange getYearRange() {
            return yearRange;
        }

        public void setYearRange(PrequalificationYearRange yearRange) {
            this.yearRange = yearRange;
        }

        public List<PrequalificationSchemaItem> getItems() {
            return items;
        }

        public void setItems(
                List<PrequalificationSchemaItem> items) {
            this.items = items;
        }

        public List<Supplier> getSuppliers() {
            return suppliers;
        }

        public void setSuppliers(List<Supplier> suppliers) {
            this.suppliers = suppliers;
        }

        public List<TargetGroup> getTargetGroups() {
            return targetGroups;
        }

        public void setTargetGroups(List<TargetGroup> targetGroups) {
            this.targetGroups = targetGroups;
        }

        public List<Subcounty> getSubcounties() {
            return subcounties;
        }

        public void setSubcounties(List<Subcounty> subcounties) {
            this.subcounties = subcounties;
        }

        public List<Ward> getWards() {
            return wards;
        }

        public void setWards(List<Ward> wards) {
            this.wards = wards;
        }

        @Override
        public Specification<PrequalifiedSupplierItem> getSpecification() {
            return (root, cq, cb) -> {

                Subquery<PrequalifiedSupplier> sub = cq.subquery(PrequalifiedSupplier.class).distinct(true);
                Root<PrequalifiedSupplier> subRoot = sub.from(PrequalifiedSupplier.class);
                sub.select(subRoot);

                List<Predicate> subPredicates = new ArrayList<>();

                if (yearRange != null) {
                    subPredicates.add(cb.equal(subRoot.get(PrequalifiedSupplier_.yearRange), yearRange));
                }

                Join<PrequalifiedSupplier, Supplier> supplierJoin = subRoot.join(PrequalifiedSupplier_.supplier);

                if (!targetGroups.isEmpty()) {
                    ListJoin<Supplier, TargetGroup> targetGroupJoin = supplierJoin.join(Supplier_.targetGroups);
                    subPredicates.add(targetGroupJoin.in(targetGroups));
                }

                if (!suppliers.isEmpty()) {
                    subPredicates.add(supplierJoin.in(suppliers));
                }

                if (!subcounties.isEmpty()) {
                    subPredicates.add(supplierJoin.join(Supplier_.subcounties).in(subcounties));
                }

                if (!wards.isEmpty()) {
                    subPredicates.add(supplierJoin.join(Supplier_.wards).in(wards));
                }

                sub.where(subPredicates.toArray(new Predicate[0]));

                List<Predicate> predicates = new ArrayList<>();

                predicates.add(root.get(PrequalifiedSupplierItem_.parent).in(sub));

                if (!items.isEmpty()) {
                    predicates.add(root.join(PrequalifiedSupplierItem_.item).in(items));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };
        }
    }

    @Override
    protected void addColumns() {
        columns.add(new AbstractColumn<PrequalifiedSupplierItem, String>(new StringResourceModel("item", this)) {

            @Override
            public void populateItem(Item<ICellPopulator<PrequalifiedSupplierItem>> cellItem, String componentId,
                    IModel<PrequalifiedSupplierItem> rowModel) {
                cellItem.add(new Label(componentId, new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        PrequalifiedSupplierItem entity = rowModel.getObject();
                        return entity.getItem().toString(entity.getParent().getYearRange());
                    }
                }));
            }
        });

        columns.add(new PropertyColumn<>(
                new StringResourceModel("supplier", this), "parent.supplier"));

        columns.add(new LambdaColumn<>(
                new StringResourceModel("targetGroup", this),
                item -> item.getParent().getSupplier().getTargetGroups().stream()
                        .map(Category::toString)
                        .collect(Collectors.joining(", "))));

        columns.add(new LambdaColumn<>(
                new StringResourceModel("subcounties", this),
                item -> item.getParent().getSupplier().getSubcounties().stream()
                        .map(Category::toString)
                        .collect(Collectors.joining(", "))));

        columns.add(new LambdaColumn<>(
                new StringResourceModel("wards", this),
                item -> item.getParent().getSupplier().getWards().stream()
                        .map(Category::toString)
                        .collect(Collectors.joining(", "))));

        columns.add(new PropertyColumn<>(
                new StringResourceModel("directors", this), "nonNullContact.directors"));

        columns.add(new PropertyColumn<>(
                new StringResourceModel("email", this), "nonNullContact.email"));

        columns.add(new PropertyColumn<>(
                new StringResourceModel("phoneNumber", this), "nonNullContact.phoneNumber"));

        columns.add(new PropertyColumn<>(
                new StringResourceModel("mailingAddress", this), "nonNullContact.mailingAddress"));

        StringResourceModel actionsColumn = new StringResourceModel("actionsColumn", this);
        columns.add(new AbstractColumn<PrequalifiedSupplierItem, String>(actionsColumn) {
            @Override
            public void populateItem(Item<ICellPopulator<PrequalifiedSupplierItem>> cellItem, String componentId,
                    IModel<PrequalifiedSupplierItem> rowModel) {

                cellItem.add(new ActionPanel(componentId, rowModel));
            }
        });
    }

    @Override
    protected Component createAddButton(String id) {
        BootstrapAjaxLink<Void> button = new BootstrapAjaxLink<Void>(id, Buttons.Type.Success) {

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(submittedSchemaModel.getObject());
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                PageParameters params = new PageParameters();
                PrequalificationYearRange yearRange = filterModel.getObject().getYearRange();
                if (yearRange != null) {
                    params.add("yearRangeId", yearRange.getId());
                }
                setResponsePage(EditPrequalifiedSupplierPage.class, params);
            }
        };

        button.add(newDraftSchemaWarningTooltipBehavior());

        return button
                .setIconType(FontAwesomeIconType.plus_circle)
                .setLabel(new StringResourceModel("new"));
    }

    private TooltipBehavior newDraftSchemaWarningTooltipBehavior() {
        StringResourceModel warning = new StringResourceModel("draftSchemaWarning", this);
        return new TooltipBehavior(
                submittedSchemaModel.combineWith(warning, (submitted, msg) -> submitted ? "" : msg));
    }

    @Override
    public JpaFilterState<PrequalifiedSupplierItem> newFilterState() {
        return filterModel.getObject();
    }

    private final class ActionPanel extends GenericPanel<PrequalifiedSupplierItem> {

        private ActionPanel(String id, IModel<PrequalifiedSupplierItem> model) {
            super(id, model);

            PageParameters params = new PageParameters();
            params.add("id", model.getObject().getParent().getId());

            BootstrapBookmarkablePageLink<EditPrequalifiedSupplierPage> editPageLink;
            editPageLink = new BootstrapBookmarkablePageLink<>(
                    "edit", EditPrequalifiedSupplierPage.class, params, Buttons.Type.Primary);
            editPageLink.setEnabled(submittedSchemaModel.getObject());
            editPageLink.add(newDraftSchemaWarningTooltipBehavior());
            editPageLink.setIconType(FontAwesomeIconType.edit)
                    .setSize(Buttons.Size.Small)
                    .setLabel(new StringResourceModel("edit", ListPrequalifiedSupplierPage.this, null));
            add(editPageLink);

            BootstrapAjaxLink<Void> deleteItemLink = new BootstrapAjaxLink<Void>("delete", Buttons.Type.Danger) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    itemToDeleteModel = model;
                    deleteModal.show(target);
                }
            };
            deleteItemLink.setEnabled(submittedSchemaModel.getObject());
            deleteItemLink.add(newDraftSchemaWarningTooltipBehavior());
            deleteItemLink.setIconType(FontAwesomeIconType.trash)
                    .setSize(Buttons.Size.Small)
                    .setLabel(new StringResourceModel("delete", ListPrequalifiedSupplierPage.this, null));
            add(deleteItemLink);
        }
    }

    protected TextContentModal createDeleteModal() {
        final TextContentModal modal = new TextContentModal("deleteModal",
                new StringResourceModel("confirmDeleteModal.content", this));
        modal.addCloseButton();

        final LaddaAjaxLink<Void> deleteButton = new LaddaAjaxLink<Void>("button", Buttons.Type.Danger) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.appendCloseDialogJavaScript(target);
                onDelete(target);
            }
        };
        deleteButton.setLabel(new StringResourceModel("confirmDeleteModal.delete", this));
        modal.addButton(deleteButton);

        return modal;
    }

    private void onDelete(AjaxRequestTarget target) {
        PrequalifiedSupplierItem item = itemToDeleteModel.getObject();
        PrequalifiedSupplier prequalifiedSupplier = item.getParent();
        if (prequalifiedSupplier.getItems().size() == 1) {
            prequalifiedSupplierService.delete(prequalifiedSupplier);
        } else {
            prequalifiedSupplier.getItems().remove(item);
            prequalifiedSupplierService.save(prequalifiedSupplier);
        }

        itemToDeleteModel = null;

        target.add(getDataTable());
    }
}
