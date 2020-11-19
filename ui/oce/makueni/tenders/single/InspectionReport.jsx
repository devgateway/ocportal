import React from 'react';
import moment from 'moment';
import AuthImplReport from './AuthImplReport';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class InspectionReport extends AuthImplReport {

  getReportName() {
    return 'Inspection Reports';
  }

  getFMPrefix() {
    return "inspectionReportForm"
  }

  authChildren(i) {
    const { currencyFormatter } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return (<div>
        {isFeatureVisible("inspectionReportForm.tenderProcess.singleContract.contractValue")
        && <Item label="Contract Sum" value={currencyFormatter(i.contract.contractValue)} col={3} />}
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
        <div className="row padding-top-10">
          {isFeatureVisible("inspectionReportForm.comments")
          && <Item label="Comments" value={i.comments} col={6} />}

          {
            i.privateSectorRequests !== undefined
            && isFeatureVisible("inspectionReportForm.privateSectorRequests")
            && isFeatureVisible("inspectionReportForm.privateSectorRequests.requestDate") ?
              <Item label="No of days for the inspection report to be done"
                    value={this.getNearestRequestDate(i)} col={6} />
              : null
          }
        </div>
        {
          i.privateSectorRequests !== undefined && isFeatureVisible("inspectionReportForm.privateSectorRequests") ?
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Private Sector Requests
                ({i.privateSectorRequests.length})
              </div>
              {i.privateSectorRequests.map(psr => <div key={psr._id} className="box">
                <div className="row padding-top-10">
                  {isFeatureVisible("inspectionReportForm.privateSectorRequests.upload")
                  && <Item label="Private Sector Request" col={3}>
                    {
                      psr.upload.map(doc => <div key={doc._id}>
                        <OverlayTrigger
                          placement="bottom"
                          overlay={
                            <Tooltip id="download-tooltip">
                              Click to download the file
                            </Tooltip>
                          }>

                          <a className="download" href={doc.url} target="_blank">
                            <i className="glyphicon glyphicon-download"/>
                            <span>{doc.name}</span>
                          </a>
                        </OverlayTrigger>
                      </div>)
                    }
                  </Item>}

                  {isFeatureVisible("inspectionReportForm.privateSectorRequests.requestDate")
                  && <Item label="Request Date" value={formatDate(psr.requestDate)} col={3} />}
                </div>
              </div>)
              }
            </div> : null
        }
      </div>)];
  }
}

export default fmConnect(InspectionReport);
