import React from 'react';
import AuthImplReport from './AuthImplReport';

class PMCReport extends AuthImplReport {

  getReportName() {
    return this.t("pmcReport:label");
  }

  authChildren(i) {
    return (<div>
        <div className="col-md-3">
          <div className="item-label">{this.t("pmcReport:subcounties")}</div>
          <div className="item-value">{i.subcounties.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("pmcReport:wards")}</div>
          <div className="item-value">{i.wards.map(item => item.label)
            .join(', ')}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">{this.t("pmcReport:pmcStatus")}</div>
          <div className="item-value">{i.pmcStatus.label}</div>
        </div>
      </div>
    );
  }

  childElements(i) {
    return [super.childElements(i),
      <div key="2">
        <div className="row padding-top-10">
          <div className="col-md-3">
            <div className="item-label">{this.t("pmcReport:projectClosureAndHandover")}</div>
            <div className="item-value">{i.projectClosureHandover && i.projectClosureHandover.map(item => item.label)
                .join(', ')}</div>
          </div>
        </div>
        <div className="row padding-top-10">
          <div className="col-md-10">
            <div className="item-label">{this.t("pmcReport:members")}</div>
          </div>
        </div>
        {
          i.pmcMembers && i.pmcMembers.map(m => <div key={m._id} className="row padding-top-10">
            <div className="col-md-3">
              <div className="item-label">{this.t("pmcReport:staff")}</div>
              <div className="item-value">{m.staff.label}</div>
            </div>
            <div className="col-md-3">
              <div className="item-label">{this.t("pmcReport:designation")}</div>
              <div className="item-value">{m.designation.label}</div>
            </div>
          </div>)
        }
      </div>,
      <div key="3">
        <div className="row padding-top-10">
          <div className="col-md-3">
            <div className="item-label">Social Safeguards</div>
            <div
                className="item-value">{i.socialSafeguards}</div>
          </div>
          <div className="col-md-3">
            <div className="item-label">Emerging Complaints</div>
            <div
                className="item-value">{i.emergingComplaints}</div>
          </div>
          <div className="col-md-3">
            <div className="item-label">PMC Challenges</div>
            <div
                className="item-value">{i.pmcChallenges}</div>
          </div>
        </div>

      </div>,
      <div key="4">
        <div className="row padding-top-10">
          <div className="col-md-12">
            <div className="item-label">PMC Notes</div>
            <div className="item-value">{i.pmcNotes && i.pmcNotes.map(item => item.text)
                .join(', ')}</div>
          </div>
        </div>

      </div>
    ];
  }
}

export default PMCReport;
