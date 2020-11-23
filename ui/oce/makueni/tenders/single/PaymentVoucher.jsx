import React from 'react';
import ImplReport from './ImplReport';

class PaymentVoucher extends ImplReport {

  getReportName() {
    return this.t("paymentVoucher:label");
  }


  childElements(i) {
    const { currencyFormatter, formatBoolean } = this.props.styling.tables;
    return (<div>
          <div className="row padding-top-10">
            <div className="col-md-3">
              <div className="item-label">{this.t("paymentVoucher:pmcReport")}</div>
              <div className="item-value">{i.pmcReport.label}</div>
            </div>
            <div className="col-md-3">
              <div className="item-label">{this.t("paymentVoucher:inspectionReport")}</div>
              <div className="item-value">{i.inspectionReport.label}</div>
            </div>
            <div className="col-md-3">
              <div className="item-label">{this.t("paymentVoucher:administratorReport")}</div>
              <div className="item-value">{i.administratorReport.label}</div>
            </div>
            <div className="col-md-3">
              <div className="item-label">{this.t("paymentVoucher:totalAmount")}</div>
              <div className="item-value">{currencyFormatter(i.totalAmount)}</div>
            </div>
          </div>
           <div className="row padding-top-10">
              <div className="col-md-3">
                 <div className="item-label">{this.t("paymentVoucher:lastPayment")}</div>
                 <div className="item-value">{formatBoolean(i.lastPayment)}</div>
              </div>
             <div className="col-md-3">
               <div className="item-label">{this.t("paymentVoucher:contractNumber")}</div>
               <div className="item-value">{currencyFormatter(i.contract.referenceNumber)}</div>
             </div>
           </div>
        </div>);
  }

}

export default PaymentVoucher;
