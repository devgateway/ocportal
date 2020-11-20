import React from 'react';
import AuthImplReport from './AuthImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class PMCReport extends AuthImplReport {

  getReportName() {
    return 'PMC Reports';
  }

  getFMPrefix() {
    return "publicView.pmcReport"
  }

  authChildren(i) {
    const { isFeatureVisible } = this.props;
    return (<div>
        {isFeatureVisible("publicView.pmcReport.subcounties")
        && <Item label="Sub-Counties" value={i.subcounties.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.pmcReport.wards")
        && <Item label="Wards" value={i.wards.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.pmcReport.pmcStatus")
        && <Item label="PMC Status" value={i.pmcStatus.label} col={3} />}
      </div>
    );
  }

  childElements(i) {
    const { isFeatureVisible } = this.props;
    return [super.childElements(i),
      <div key="2">
        <div className="row">
          {isFeatureVisible("publicView.pmcReport.projectClosureHandover")
          && <Item label="Project Closure and Handover"
                   value={i.projectClosureHandover && i.projectClosureHandover.map(item => item.label).join(', ')}
                   col={3} />}
        </div>
        {isFeatureVisible("publicView.pmcReport.pmcMembers")
        && <React.Fragment>
          <div className="row padding-top-10">
            <div className="col-md-10">
              <div className="item-label">PMC MEMBERS:</div>
            </div>
          </div>
          {
            i.pmcMembers && i.pmcMembers.map(m => <div key={m._id} className="row">
              {isFeatureVisible("publicView.pmcReport.pmcMembers.staff")
              && <Item label="PMC Staff" value={m.staff.label} col={3} />}

              {isFeatureVisible("publicView.pmcReport.pmcMembers.designation")
              && <Item label="PMC Designation" value={m.designation.label} col={3} />}
            </div>)
          }
        </React.Fragment>}
      </div>,
      <div key="3">
        <div className="row">
          {isFeatureVisible("publicView.pmcReport.socialSafeguards")
          && <Item label="Social Safeguards" value={i.socialSafeguards} col={3} />}

          {isFeatureVisible("publicView.pmcReport.emergingComplaints")
          && <Item label="Emerging Complaints" value={i.emergingComplaints} col={3} />}

          {isFeatureVisible("publicView.pmcReport.pmcChallenges")
          && <Item label="PMC Challenges" value={i.pmcChallenges} col={3} />}
        </div>
      </div>,
      <div key="4">
        {isFeatureVisible("publicView.pmcReport.pmcNotes")
        && <div className="row">
          <Item label="PMC Notes" value={i.pmcNotes && i.pmcNotes.map(item => item.text).join(', ')} col={12} />
        </div>}
      </div>
    ];
  }
}

export default fmConnect(PMCReport);
