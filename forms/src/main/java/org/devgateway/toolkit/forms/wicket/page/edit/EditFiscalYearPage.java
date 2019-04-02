package org.devgateway.toolkit.forms.wicket.page.edit;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.lists.ListFiscalYearPage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/fiscalYear")
public class EditFiscalYearPage extends AbstractEditPage<FiscalYear> {

    @SpringBean
    private FiscalYearService service;

    private DateFieldBootstrapFormComponent endDate;

    private DateFieldBootstrapFormComponent startDate;

    public EditFiscalYearPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListFiscalYearPage.class;
    }

    public void addName() {
        TextFieldBootstrapFormComponent<String> name = new TextFieldBootstrapFormComponent<>("name");
        name.required();
        editForm.add(name);
    }


    public void addStartDate() {
        startDate = new DateFieldBootstrapFormComponent("startDate");
        startDate.required();
        editForm.add(startDate);
    }

    public void addEndDate() {
        endDate = new DateFieldBootstrapFormComponent("endDate");
        endDate.required();
        editForm.add(endDate);
    }


    public void addStartEndDatesValidator() {
        editForm.add(new AbstractFormValidator() {
            @Override
            public FormComponent<?>[] getDependentFormComponents() {
                return null;
            }

            @Override
            public void validate(final Form<?> form) {
                if (editForm.getModelObject().getStartDate() != null && editForm.getModelObject()
                        .getEndDate() != null && editForm.getModelObject().getStartDate().
                                after(editForm.getModelObject().getEndDate())) {
                    editForm.error(getString("startDateBeforeEndDate"));
                }

            }
        });

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addName();
        addStartDate();
        addEndDate();
        addStartEndDatesValidator();
    }
}
