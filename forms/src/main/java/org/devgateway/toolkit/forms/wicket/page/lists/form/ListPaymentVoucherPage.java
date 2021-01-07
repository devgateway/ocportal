package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPaymentVoucherPage;
import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.PaymentVoucherFilterState;
import org.devgateway.toolkit.persistence.service.form.PaymentVoucherService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class ListPaymentVoucherPage extends ListAbstractImplTenderProcessMakueniEntity<PaymentVoucher> {

    @SpringBean
    protected PaymentVoucherService service;

    public ListPaymentVoucherPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
        this.editPageClass = EditPaymentVoucherPage.class;
    }

    @Override
    protected void onInitialize() {
        attachFm("paymentVouchersList");
        addTenderTitleColumn();

        super.onInitialize();
    }


    @Override
    public JpaFilterState<PaymentVoucher> newFilterState() {
        return new PaymentVoucherFilterState();
    }
}
