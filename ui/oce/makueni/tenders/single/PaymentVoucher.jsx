import React from 'react';
import AuthImplReport from './AuthImplReport';
import ImplReport from './ImplReport';

class PaymentVoucher extends ImplReport {

  getReportName() {
    return 'Payment Vouchers';
  }


  childElements(i) {
    const { currencyFormatter, formatBoolean } = this.props.styling.tables;
    return (<div>
          <div className="row padding-top-10">
            <div className="col-md-4">
              <div className="item-label">PMC Report</div>
              <div className="item-value">{i.pmcReport.label}</div>
            </div>
            <div className="col-md-4">
              <div className="item-label">Inspection Report</div>
              <div className="item-value">{i.inspectionReport.label}</div>
            </div>
            <div className="col-md-4">
              <div className="item-label">Administrator Report</div>
              <div className="item-value">{i.administratorReport.label}</div>
            </div>
          </div>
           <div className="row padding-top-10">
              <div className="col-md-4">
                 <div className="item-label">Total Amount</div>
                 <div className="item-value">{currencyFormatter(i.totalAmount)}</div>
              </div>
              <div className="col-md-4">
                 <div className="item-label">Last Payment</div>
                 <div className="item-value">{formatBoolean(i.lastPayment)}</div>
              </div>
           </div>
        </div>);
  }

}

export default PaymentVoucher;
