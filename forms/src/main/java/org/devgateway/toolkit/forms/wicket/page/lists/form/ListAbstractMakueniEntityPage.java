package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListStatusEntityPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class ListAbstractMakueniEntityPage<T extends AbstractMakueniEntity>
        extends AbstractListStatusEntityPage<T> {
    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    protected final List<Department> departments;

    protected final List<FiscalYear> fiscalYears;

    public ListAbstractMakueniEntityPage(final PageParameters parameters) {
        super(parameters);

        this.departments = departmentService.findAll();
        this.fiscalYears = fiscalYearService.findAll();
    }

    @Override
    protected void onInitialize() {
        // just replace the page title with the name of the class
        // instead of having .properties files only for the page title
        addOrReplace(new Label("pageTitle",
                StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                        this.getClass().getSimpleName().replaceAll("List", "").replaceAll("Page", "")), ' ')
                        + " List"));

        super.onInitialize();

        // don't allow users to add new entities from the listing pages for AbstractMakueniEntity.
        editPageLink.setVisibilityAllowed(false);
    }
}
