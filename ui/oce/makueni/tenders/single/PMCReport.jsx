import React from 'react';
import AuthImplReport from './AuthImplReport';

class PMCReport extends AuthImplReport {

  getReportName() {
    return 'PMC Reports';
  }

  childElements(i) {
    return super.childElements(i) && (
      <div>
        <div className="row padding-top-10">
          <div className="col-md-4">
            <div className="item-label">Sub-Counties</div>
            <div className="item-value">{i.subcounties.map(item => item.label)
              .join(', ')}</div>
          </div>
          <div className="col-md-4">
            <div className="item-label">Wards</div>
            <div className="item-value">{i.wards.map(item => item.label)
              .join(', ')}</div>
          </div>
          <div className="col-md-4">
            <div className="item-label">PMC Status</div>
            <div className="item-value">{i.pmcStatus.label}</div>
          </div>
        </div>
        <div className="row padding-top-10">
          <div className="col-md-4">
            <div className="item-label">Project Closure and Handover</div>
            <div className="item-value">{i.projectClosureHandover.label}</div>
          </div>
        </div>
        <div className="row padding-top-10">
          <div className="col-md-10">
            <div className="item-label">PMC MEMBERS:</div>
          </div>
        </div>
        {
          i.pmcMembers && i.pmcMembers.map(m => <div className="row padding-top-10" key={m._id}>
            <div className="col-md-4">
              <div className="item-label">PMC Staff</div>
              <div className="item-value">{m.staff.label}</div>
            </div>
            <div className="col-md-4">
              <div className="item-label">Designation</div>
              <div className="item-value">{m.designation.label}</div>
            </div>
          </div>)
        }
      </div>);
  }
}

export default PMCReport;
