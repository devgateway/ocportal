import React from 'react';
import moment from 'moment';
import AuthImplReport from './AuthImplReport';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class InspectionReport extends AuthImplReport {

  getReportName() {
    return this.t("inspectionReport:label");
  }

  authChildren(i) {
    const { currencyFormatter } = this.props.styling.tables;
    return (<div>
        <div className="col-md-3">
          <div className="item-label">{this.t("inspectionReport:contractValue")}</div>
          <div className="item-value">{currencyFormatter(i.contract.contractValue)}</div>
        </div>
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
    return [super.childElements(i),
      (<div key="2">
        <div className="row padding-top-10">
          <div className="col-md-6">
            <div className="item-label">{this.t("inspectionReport:comments")}</div>
            <div className="item-value">{i.comments}</div>

          </div>
          {
            i.privateSectorRequests !== undefined ?
              <div className="col-md-6">
                <div className="item-label">{this.t("inspectionReport:daysUntilReport")}</div>
                <div
                  className="item-value">{this.getNearestRequestDate(i)}</div>
              </div> : null
          }
        </div>
        {
          i.privateSectorRequests !== undefined ?
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("inspectionReport:privateSectorRequest:pl")}
                ({i.privateSectorRequests.length})
              </div>
              {i.privateSectorRequests.map(psr => <div key={psr._id} className="box">
                <div className="row padding-top-10">
                  <div className="col-md-3">
                    <div className="item-label">{this.t("inspectionReport:privateSectorRequest:sg")}</div>
                    {
                      psr.upload.map(doc => <div key={doc._id}>
                        <OverlayTrigger
                          placement="bottom"
                          overlay={
                            <Tooltip id="download-tooltip">
                              {this.t("general:downloadFile:tooltip")}
                            </Tooltip>
                          }>

                          <a className="item-value download" href={doc.url} target="_blank">
                            <i className="glyphicon glyphicon-download"/>
                            <span>{doc.name}</span>
                          </a>
                        </OverlayTrigger>
                      </div>)
                    }
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("inspectionReport:privateSectorRequest:requestDate")}</div>
                    <div className="item-value">{formatDate(psr.requestDate)}</div>
                  </div>
                </div>
              </div>)
              }
            </div> : null
        }
      </div>)];
  }
}

export default InspectionReport;
