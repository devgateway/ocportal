/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.forms.wicket.page.lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Size;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.exceptions.NullEditPageClassException;
import org.devgateway.toolkit.forms.exceptions.NullJpaServiceException;
import org.devgateway.toolkit.forms.service.PermissionEntityRenderableService;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.RevisionsPage;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.providers.SortableJpaServiceDataProvider;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.excel.service.ExcelGeneratorService;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author mpostelnicu This class can be use to display a list of Categories
 * <p>
 */
public abstract class AbstractListPage<T extends GenericPersistable & Serializable> extends BasePage {
    protected Class<? extends AbstractEditPage<T>> editPageClass;

    protected final AjaxFallbackBootstrapDataTable<T, String> dataTable;

    protected final List<IColumn<T, String>> columns;

    protected BaseJpaService<T> jpaService;

    private final SortableJpaServiceDataProvider<T> dataProvider;

    private BootstrapBookmarkablePageLink<T> editPageLink;

    protected Form excelForm;

    @SpringBean
    private ExcelGeneratorService excelGeneratorService;

    @SpringBean
    private PermissionEntityRenderableService permissionEntityRenderableService;

    public AbstractListPage(final PageParameters parameters) {
        super(parameters);

        columns = new ArrayList<>();
        dataProvider = new SortableJpaServiceDataProvider<>();
        dataTable = new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, WebConstants.PAGE_SIZE);

        columns.add(new AbstractColumn<T, String>(new Model<>("#")) {
            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem,
                                     final String componentId,
                                     final IModel<T> model) {
                final OddEvenItem oddEvenItem = (OddEvenItem) cellItem.getParent().getParent();
                final long index = WebConstants.PAGE_SIZE * dataTable.getCurrentPage() + oddEvenItem.getIndex() + 1;
                cellItem.add(new Label(componentId, index));
            }
        });
    }

    public ActionPanel getActionPanel(final String id, final IModel<T> model) {
        return new ActionPanel(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (jpaService == null) {
            throw new NullJpaServiceException();
        }
        if (editPageClass == null) {
            throw new NullEditPageClassException();
        }

        dataProvider.setJpaService(jpaService);
        dataProvider.setFilterState(newFilterState());

        // create the excel download form; by default this form is hidden and we should make it visible only to pages
        // where we want to export entities to excel file
        excelForm = new ExcelDownloadForm("excelForm");
        excelForm.setVisibilityAllowed(false);
        add(excelForm);

        // add the 'Edit' button
        columns.add(new AbstractColumn<T, String>(new StringResourceModel("actionsColumn", this, null)) {
            private static final long serialVersionUID = -7447601118569862123L;

            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId,
                                     final IModel<T> model) {
                cellItem.add(getActionPanel(componentId, model));
            }
        });

        final ResettingFilterForm<JpaFilterState<T>> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);
        filterForm.add(dataTable);

        // create custom submit button in order to prevent form submission
        final LaddaAjaxButton submit = new LaddaAjaxButton("submit",
                new Model<>("Submit"), Buttons.Type.Default) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                // don't do anything on submit, just refresh the table
                target.add(dataTable);
            }
        };
        filterForm.add(dataTable);
        filterForm.add(submit);
        filterForm.setDefaultButton(submit);

        add(filterForm);

        if (hasFilteredColumns()) {
            dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm));
        }

        editPageLink = new BootstrapBookmarkablePageLink<>("new", editPageClass, Buttons.Type.Success);
        editPageLink.setIconType(FontAwesomeIconType.plus_circle).setSize(Size.Large)
                .setLabel(new StringResourceModel("new", AbstractListPage.this, null));

        add(editPageLink);
    }

    public class ActionPanel extends Panel {
        private static final long serialVersionUID = 5821419128121941939L;

        /**
         * @param id
         * @param model
         */
        public ActionPanel(final String id, final IModel<T> model) {
            super(id, model);

            final PageParameters pageParameters = new PageParameters();

            @SuppressWarnings("unchecked")
            T entity = (T) ActionPanel.this.getDefaultModelObject();
            if (entity != null) {
                pageParameters.set(WebConstants.PARAM_ID, entity.getId());
            }

            final BootstrapBookmarkablePageLink<T> editPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", editPageClass, pageParameters, Buttons.Type.Info);
            editPageLink.setIconType(FontAwesomeIconType.edit)
                    .setSize(Size.Small)
                    .setType(Buttons.Type.Primary)
                    .setLabel(new StringResourceModel("edit", AbstractListPage.this, null));
            if (entity instanceof AbstractMakueniEntity && SecurityConstants.Action.VIEW.equals(
                    permissionEntityRenderableService.getAllowedAccess((AbstractStatusAuditableEntity) entity))) {
                editPageLink.setIconType(FontAwesomeIconType.eye)
                        .setType(Buttons.Type.Warning)
                        .setLabel(new StringResourceModel("view", AbstractListPage.this, null));
            }
            add(editPageLink);

            add(getPrintButton(pageParameters));

            final PageParameters revisionsPageParameters = new PageParameters();
            revisionsPageParameters.set(WebConstants.PARAM_ID, entity.getId());
            revisionsPageParameters.set(WebConstants.PARAM_ENTITY_CLASS, entity.getClass().getName());

            final BootstrapBookmarkablePageLink<Void> revisionsPageLink = new BootstrapBookmarkablePageLink<>(
                    "revisions", RevisionsPage.class, revisionsPageParameters, Buttons.Type.Info);
            revisionsPageLink.setIconType(FontAwesomeIconType.clock_o).setSize(Size.Small)
                    .setLabel(new StringResourceModel("revisions", AbstractListPage.this, null));
            add(revisionsPageLink);
            MetaDataRoleAuthorizationStrategy.authorize(
                    revisionsPageLink, Component.RENDER, SecurityConstants.Roles.ROLE_ADMIN);
        }
    }

    /**
     * Get a stub print button that does nothing
     *
     * @param pageParameters
     * @return
     */
    protected Component getPrintButton(final PageParameters pageParameters) {
        return new WebMarkupContainer("printButton").setVisibilityAllowed(false);
    }

    private boolean hasFilteredColumns() {
        for (final IColumn<?, ?> column : columns) {
            if (column instanceof IFilteredColumn) {
                return true;
            }
        }
        return false;
    }

    public JpaFilterState<T> newFilterState() {
        return new JpaFilterState<>();
    }

    /**
     * A wrapper form that is used to fire the excel download action
     */
    public class ExcelDownloadForm extends Form<Void> {
        public ExcelDownloadForm(final String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final AJAXDownload download = new AJAXDownload() {
                @Override
                protected IRequestHandler getHandler() {
                    return new IRequestHandler() {
                        @Override
                        public void respond(final IRequestCycle requestCycle) {
                            final HttpServletResponse response = (HttpServletResponse) requestCycle
                                    .getResponse().getContainerResponse();

                            try {
                                final int batchSize = 10000;

                                final long count = excelGeneratorService.count(
                                        jpaService,
                                        dataProvider.getFilterState().getSpecification());

                                // if we need to export just one file then we don't create an archive
                                if (count <= batchSize) {
                                    // set a maximum download of objects per excel file
                                    final PageRequest pageRequest = PageRequest.of(0, batchSize,
                                            Sort.Direction.ASC, "id");

                                    final byte[] bytes = excelGeneratorService.getExcelDownload(
                                            jpaService,
                                            dataProvider.getFilterState().getSpecification(),
                                            pageRequest);

                                    response.setContentType(
                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.xlsx");
                                    response.getOutputStream().write(bytes);
                                } else {
                                    response.setContentType("application/zip");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.zip");
                                    response.flushBuffer();
                                    final ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(
                                            response.getOutputStream()));
                                    zout.setMethod(ZipOutputStream.DEFLATED);
                                    zout.setLevel(Deflater.NO_COMPRESSION);
                                    final int numberOfPages = (int) Math.ceil((double) count / batchSize);
                                    for (int i = 0; i < numberOfPages; i++) {
                                        final PageRequest pageRequest = PageRequest.of(i, batchSize,
                                                Sort.Direction.ASC, "id");
                                        final byte[] bytes = excelGeneratorService.getExcelDownload(
                                                jpaService,
                                                dataProvider.getFilterState().getSpecification(),
                                                pageRequest);
                                        final ZipEntry ze = new ZipEntry("excel-export-page " + (i + 1) + ".xlsx");
                                        zout.putNextEntry(ze);
                                        zout.write(bytes, 0, bytes.length);
                                        zout.closeEntry();
                                        response.flushBuffer();
                                    }
                                    zout.close();
                                    response.flushBuffer();
                                }
                            } catch (IOException e) {
                                logger.error("Download error", e);
                            }

                            RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);
                        }

                        @Override
                        public void detach(final IRequestCycle requestCycle) {
                            // do nothing;
                        }
                    };
                }
            };
            add(download);

            final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton",
                    new Model<>("Excel Download"),
                    Buttons.Type.Warning) {
                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    // initiate the file download
                    download.initiate(target);
                }
            };
            excelButton.setIconType(FontAwesomeIconType.file_excel_o);
            add(excelButton);
        }
    }
}
