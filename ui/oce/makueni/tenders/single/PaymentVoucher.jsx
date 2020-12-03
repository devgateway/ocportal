import React from 'react';
import ImplReport from './ImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class PaymentVoucher extends ImplReport {

  getReportName() {
    return this.t("paymentVoucher:label");
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
        && <Item label={this.t("paymentVoucher:pmcReport")} value={i.pmcReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.inspectionReport")
        && <Item label={this.t("paymentVoucher:inspectionReport")} value={i.inspectionReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.administratorReport")
        && <Item label={this.t("paymentVoucher:administratorReport")} value={i.administratorReport.label} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.totalAmount")
        && <Item label={this.t("paymentVoucher:totalAmount")} value={currencyFormatter(i.totalAmount)} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.lastPayment")
        && <Item label={this.t("paymentVoucher:lastPayment")} value={formatBoolean(i.lastPayment)} col={3} />}

        {isFeatureVisible("publicView.paymentVoucher.contractRefNum")
        && <Item label={this.t("paymentVoucher:contractNumber")} value={currencyFormatter(i.contract.referenceNumber)} col={3} />}
      </div>
    </div>);
  }

}

export default fmConnect(PaymentVoucher);
