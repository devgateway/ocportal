import React from 'react';
import AuthImplReport from './AuthImplReport';

class InspectionReport extends AuthImplReport {

  getReportName() {
    return "Inspection Report";
  }


  childElements(i) {
    return (<div className="row padding-top-10">
      <div className="col-md-6">
        <div className="item-label">Comments</div>
        <div className="item-value">{i.comments}</div>
      </div>
    </div>);
  }
}

export default InspectionReport;
