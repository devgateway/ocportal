import React from 'react';
import moment from 'moment';
import AuthImplReport from './AuthImplReport';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

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
            <div className="item-label">Comments</div>
            <div className="item-value">{i.comments}</div>

          </div>
          {
            i.privateSectorRequests !== undefined ?
              <div className="col-md-6">
                <div className="item-label">No of days for the inspection report to be done</div>
                <div
                  className="item-value">{this.getNearestRequestDate(i)}</div>
              </div> : null
          }
        </div>
        {
          i.privateSectorRequests !== undefined ?
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Private Sector Requests
                ({i.privateSectorRequests.length})
              </div>
              {i.privateSectorRequests.map(psr => <div key={psr._id} className="box">
                <div className="row padding-top-10">
                  <div className="col-md-3">
                    <div className="item-label">Private Sector Request</div>
                    {
                      psr.upload.map(doc => <div key={doc._id}>
                        <OverlayTrigger
                          placement="bottom"
                          overlay={
                            <Tooltip id="download-tooltip">
                              Click to download the file
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
                    <div className="item-label">Request Date</div>
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
