package org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification.EditPrequalificationSchemaPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.prequalification.PrequalificationSchemaFilterState;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.data.domain.Sort;
import org.wicketstuff.annotation.mount.MountPath;

import javax.persistence.criteria.JoinType;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListPrequalificationSchemaPage extends AbstractListStatusEntityPage<PrequalificationSchema> {

    @SpringBean
    private PrequalificationSchemaService prequalificationSchemaService;

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;


    public ListPrequalificationSchemaPage(PageParameters parameters) {
        super(parameters);
        this.jpaService = prequalificationSchemaService;
        this.editPageClass = EditPrequalificationSchemaPage.class;
        filterGoReset = true;
    }

    @Override
    protected void onInitialize() {
        addFmColumn("name", new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("name",
                        ListPrequalificationSchemaPage.this)).getString()),
                "name", "name"));
        addFmColumn("prefix", new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("prefix",
                        ListPrequalificationSchemaPage.this)).getString()),
                "prefix", "prefix"));

        addFmColumn("prequalificationYearRanges", new SelectFilteredBootstrapPropertyColumn<>(
                new StringResourceModel("prequalificationYearRanges", this),
                "prequalificationYearRanges", "prequalificationYearRanges",
                new ListModel<>(prequalificationYearRangeService.findAll()), getDataTable()
        ));

        super.onInitialize();
        topEditPageLink.setVisibilityAllowed(false);
    }

    @Override
    public JpaFilterState<PrequalificationSchema> newFilterState() {
        return new PrequalificationSchemaFilterState();
    }
}
