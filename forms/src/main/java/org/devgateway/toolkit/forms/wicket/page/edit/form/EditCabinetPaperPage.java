package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper_;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/cabinetPaper")
public class EditCabinetPaperPage extends AbstractEditPage<CabinetPaper> {
    
    @SpringBean
    protected CabinetPaperService cabinetPaperService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    public EditCabinetPaperPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = cabinetPaperService;
        this.listPageClass = ListCabinetPaperPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();       
        
        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();       
        ComponentUtil.addTextField(editForm, "name").required();
        final TextFieldBootstrapFormComponent<String> numberField = ComponentUtil.addTextField(editForm, "number");
        numberField.required();
        numberField.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueCabinetPaperNumber"),
                cabinetPaperService::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                CabinetPaper_.number), v), editForm.getModel()
        ));
        
        
        final FileInputBootstrapFormComponent doc =
                new FileInputBootstrapFormComponent("cabinetPaperDocs");
        doc.maxFiles(1);
        doc.required();
        editForm.add(doc);

    }

}
