import React from 'react';
import AuthImplReport from './AuthImplReport';

class InspectionReport extends AuthImplReport {

  getReportName() {
    return 'Inspection Reports';
  }

  authChildren(i) {
    const { currencyFormatter } = this.props.styling.tables;
    return (<div>
        <div className="col-md-3">
          <div className="item-label">Contract Sum</div>
          <div className="item-value">{currencyFormatter(i.contract.contractValue)}</div>
        </div>
      </div>
    );
  }


  childElements(i) {
    return [super.childElements(i),
      (<div key="2" className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Comments</div>
          <div className="item-value">{i.comments}</div>
        </div>
      </div>)];
  }
}

export default InspectionReport;
