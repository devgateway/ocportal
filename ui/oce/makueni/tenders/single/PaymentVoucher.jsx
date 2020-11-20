import React from 'react';
import ImplReport from './ImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class PaymentVoucher extends ImplReport {

  getReportName() {
    return 'Payment Vouchers';
  }

  getFMPrefix() {
    return "publicView.paymentVoucher"
  }

  childElements(i) {
    const { currencyFormatter, formatBoolean } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.paymentVoucher.pmcReport")
        && <Item label="PMC Report" value={i.pmcReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.inspectionReport")
        && <Item label="Inspection Report" value={i.inspectionReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.administratorReport")
        && <Item label="Administrator Report" value={i.administratorReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.totalAmount")
        && <Item label="Total Amount" value={currencyFormatter(i.totalAmount)} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.lastPayment")
        && <Item label="Last Payment" value={formatBoolean(i.lastPayment)} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.contractRefNum")
        && <Item label="Contract Number" value={currencyFormatter(i.contract.referenceNumber)} col={3} />}
      </div>
    </div>);
  }

}

export default fmConnect(PaymentVoucher);
