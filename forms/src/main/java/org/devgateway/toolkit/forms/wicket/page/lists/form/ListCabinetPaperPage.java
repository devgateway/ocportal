package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.CabinetPaperFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;



/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/cabinetPapers")
public class ListCabinetPaperPage extends AbstractListPage<CabinetPaper> {

    @SpringBean
    protected CabinetPaperService cabinetPaperService;

    public ListCabinetPaperPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = cabinetPaperService;
        this.editPageClass = EditCabinetPaperPage.class;
    }

    @Override
    protected void onInitialize() {   
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("number", ListCabinetPaperPage.this)).getString()),
                "number", "number")); 
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("name", ListCabinetPaperPage.this)).getString()),
                "name", "name"));    
        
        super.onInitialize();
    }

   @Override
    public JpaFilterState<CabinetPaper> newFilterState() {
        return new CabinetPaperFilterState();
    }
}
