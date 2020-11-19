import {OverlayTrigger, Tooltip} from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import {Item} from "./Item";

class ImplReport extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape(this.getReportName() + metadata);
  }

  getReportName(){

  }

  getFMPrefix() {
    throw new Error("you must implement this method")
  }

  render() {
    const { fiscalYear, data, tenderTitle, isFeatureVisible } = this.props;
    const { formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    return (<div>
      <div className="row padding-top-10">
        {
          data.sort((a, b) => new Date(a.approvedDate) - new Date(b.approvedDate))
              .map(i => <div key={i._id} className="box">
              <div className="row padding-top-10">
                <Item label="Tender Title" value={tenderTitle} col={3} />

                {isFeatureVisible(this.getFMPrefix() + ".tenderProcess.singleContract.awardee")
                && <Item label="Contractor" value={i.contract.awardee.label} col={3} />}

                <Item label="Fiscal Year" value={fiscalYear.name} col={3} />

                {isFeatureVisible(this.getFMPrefix() + ".approvedDate")
                && <Item label="Report Date" value={formatDate(i.approvedDate)} col={3} />}
              </div>
                {
                  this.childElements(i)
                }
                {
                  i.formDocs && isFeatureVisible(this.getFMPrefix() + ".formDocs") ?
                      <div className="row padding-top-10">
                        <Item label={this.getReportName() + " Uploads"} col={12}>
                          {
                            i.formDocs.map(doc => <div key={doc._id}>
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
                        </Item>
                      </div>
                      : null
                }
              </div>)
        }
      </div>
    </div>);
  }

  childElements(i) {

  }
}

export default ImplReport;
