package org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification.EditPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.prequalification.PrequalificationYearRangeFilterState;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListPrequalificationYearRangePage extends AbstractListPage<PrequalificationYearRange> {

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @SpringBean
    private PrequalificationSchemaService prequalificationSchemaService;

    public ListPrequalificationYearRangePage(PageParameters parameters) {
        super(parameters);
        this.jpaService = prequalificationYearRangeService;
        this.editPageClass = EditPrequalificationYearRangePage.class;
        filterGoReset = true;
    }

    @Override
    protected void addColumns() {
        addFmColumn("name", new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("name",
                        ListPrequalificationYearRangePage.this)).getString()),
                "name", "name"));
        addFmColumn("startYear", new PropertyColumn<>(new Model<>(
                (new StringResourceModel("startYear", ListPrequalificationYearRangePage.this
                )).getString()), "startYear", "startYear"));
        addFmColumn("endYear", new PropertyColumn<>(new Model<>(
                (new StringResourceModel("endYear", ListPrequalificationYearRangePage.this
                )).getString()), "endYear", "endYear"));
        addFmColumn("schema", new SelectFilteredBootstrapPropertyColumn<>(
                new StringResourceModel("schema", this),
                "schema", "schema",
                new ListModel<>(prequalificationSchemaService.findAll()), getDataTable()
        ));
    }

    @Override
    protected void onInitialize() {
        //attachFm("prequalificationSchemaList");
        super.onInitialize();
    }

    @Override
    public JpaFilterState<PrequalificationYearRange> newFilterState() {
        return new PrequalificationYearRangeFilterState();
    }
}
