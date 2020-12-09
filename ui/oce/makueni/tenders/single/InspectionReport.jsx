import React from 'react';
import moment from 'moment';
import AuthImplReport from './AuthImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

class InspectionReport extends AuthImplReport {

  getReportName() {
    return this.t("inspectionReport:label");
  }

  getFMPrefix() {
    return "publicView.inspectionReport"
  }

  authChildren(i) {
    const { currencyFormatter } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return (<div>
        {isFeatureVisible("publicView.inspectionReport.contractValue")
        && <Item label={this.t("inspectionReport:contractValue")}
                 value={currencyFormatter(i.contract.contractValue)} col={3} />}
      </div>
    );
  }

  getNearestRequestDate(i) {
    const reqDate = moment(i.privateSectorRequests.map(item => new Date(item.requestDate)).sort()[0]);
    const approvedDate = moment(i.approvedDate);

    return reqDate.diff(approvedDate, "days");
  }

  childElements(i) {
    const { formatDate } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return [super.childElements(i),
      (<div key="2">
        <div className="row">
          {isFeatureVisible("publicView.inspectionReport.comments")
          && <Item label={this.t("inspectionReport:comments")} value={i.comments} col={6} />}

          {
            i.privateSectorRequests !== undefined
            && isFeatureVisible("publicView.inspectionReport.privateSectorRequests")
            && isFeatureVisible("publicView.inspectionReport.privateSectorRequests.requestDate") ?
              <Item label={this.t("inspectionReport:daysUntilReport")}
                    value={this.getNearestRequestDate(i)} col={6} />
              : null
          }
        </div>
        {
          i.privateSectorRequests !== undefined && isFeatureVisible("publicView.inspectionReport.privateSectorRequests") ?
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("inspectionReport:privateSectorRequest:pl")}
                ({i.privateSectorRequests.length})
              </div>
              {i.privateSectorRequests.map(psr => <div key={psr._id} className="box">
                <div className="row">
                  {isFeatureVisible("publicView.inspectionReport.privateSectorRequests.upload")
                  && <Item label={this.t("inspectionReport:privateSectorRequest:sg")} col={3}>
                    <FileDownloadLinks files={psr.upload} useDash />
                  </Item>}

                  {isFeatureVisible("publicView.inspectionReport.privateSectorRequests.requestDate")
                  && <Item label={this.t("inspectionReport:privateSectorRequest:requestDate")} value={formatDate(psr.requestDate)} col={3} />}
                </div>
              </div>)
              }
            </div> : null
        }
      </div>)];
  }
}

export default fmConnect(InspectionReport);
