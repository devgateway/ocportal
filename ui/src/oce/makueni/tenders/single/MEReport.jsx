import React from 'react';
import ImplReport from './ImplReport';

class MEReport extends ImplReport {

  getReportName() {
    return this.t("meReport:label");
  }


  childElements(i) {
    const { currencyFormatter, formatDate, formatBoolean } = this.props.styling.tables;
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:sno")}</div>
          <div className="item-value">{i.sno}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:subcounties")}</div>
          <div className="item-value">{i.subcounties.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:wards")}</div>
          <div className="item-value">{i.wards.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:subwards")}</div>
          <div className="item-value">{i.subwards && i.subwards.map(item => item.label)
            .join(', ')}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:lpoAmount")}</div>
          <div className="item-value">{currencyFormatter(i.lpoAmount)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:lpoNumber")}</div>
          <div className="item-value">{i.lpoNumber}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:expenditure")}</div>
          <div className="item-value">{currencyFormatter(i.expenditure)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:uncommitted")}</div>
          <div className="item-value">{currencyFormatter(i.uncommitted)}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:projectScope")}</div>
          <div className="item-value">{i.projectScope}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:output")}</div>
          <div className="item-value">{i.output}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:outcome")}</div>
          <div className="item-value">{i.outcome}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:projectProgress")}</div>
          <div className="item-value">{i.projectProgress}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:directBeneficiariesTarget")}</div>
          <div className="item-value">{i.directBeneficiariesTarget}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:wayForward")}</div>
          <div className="item-value">{i.wayForward}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:byWhen")}</div>
          <div className="item-value">{formatDate(i.byWhen)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:inspected")}</div>
          <div className="item-value">{formatBoolean(i.inspected)}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:invoiced")}</div>
          <div className="item-value">{formatBoolean(i.invoiced)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:officerResponsible")}</div>
          <div className="item-value">{i.officerResponsible}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:meStatus")}</div>
          <div className="item-value">{i.meStatus.label}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:remarks")}</div>
          <div className="item-value">{i.remarks}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:contractorContact")}</div>
          <div className="item-value">{i.contractorContact}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:contractDate")}</div>
          <div className="item-value">{formatDate(i.contract.contractDate)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:expiryDate")}</div>
          <div className="item-value">{formatDate(i.contract.expiryDate)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("meReport:amountBudgeted")}</div>
          <div className="item-value">{currencyFormatter(i.amountBudgeted)}</div>
        </div>
      </div>
    </div>);
  }

}

export default MEReport;
