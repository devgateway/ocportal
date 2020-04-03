import React from 'react';
import AuthImplReport from './AuthImplReport';
import ImplReport from './ImplReport';

class MEReport extends ImplReport {

  getReportName() {
    return 'M&E Reports';
  }


  childElements(i) {
    const { currencyFormatter, formatDate, formatBoolean } = this.props.styling.tables;
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">SNO</div>
          <div className="item-value">{i.sno}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Sub-Counties</div>
          <div className="item-value">{i.subcounties.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Wards</div>
          <div className="item-value">{i.wards.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Sub-Wards</div>
          <div className="item-value">{i.subwards.map(item => item.label)
            .join(', ')}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">LPO Amount</div>
          <div className="item-value">{currencyFormatter(i.lpoAmount)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">LPO Number</div>
          <div className="item-value">{i.lpoNumber}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Expenditure</div>
          <div className="item-value">{currencyFormatter(i.expenditure)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Uncommitted Funds / Unabsorbed Funds / Vote Balance</div>
          <div className="item-value">{currencyFormatter(i.uncommitted)}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">Project Scope</div>
          <div className="item-value">{i.projectScope}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Output</div>
          <div className="item-value">{i.output}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Outcome</div>
          <div className="item-value">{i.outcome}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Project Progress</div>
          <div className="item-value">{i.projectProgress}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">Target number of direct beneficiaries</div>
          <div className="item-value">{i.directBeneficiariesTarget}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Way Forward</div>
          <div className="item-value">{i.wayForward}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">By When</div>
          <div className="item-value">{formatDate(i.byWhen)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Inspected</div>
          <div className="item-value">{formatBoolean(i.inspected)}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">Invoiced</div>
          <div className="item-value">{formatBoolean(i.invoiced)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Officer Responsible</div>
          <div className="item-value">{i.officerResponsible}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">M&E Status</div>
          <div className="item-value">{i.meStatus.label}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Remarks</div>
          <div className="item-value">{i.remarks}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">Contractor Contact</div>
          <div className="item-value">{i.contractorContact}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Contract Date</div>
          <div className="item-value">{formatDate(i.contract.contractDate)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Contract Expirity Date</div>
          <div className="item-value">{formatDate(i.contract.expiryDate)}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Revised Budget</div>
          <div className="item-value">{currencyFormatter(i.amountBudgeted)}</div>
        </div>
      </div>
    </div>);
  }

}

export default MEReport;
