import NoDataMessage from './NoData';
import React from 'react';
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

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
              <div className="row">
                {isFeatureVisible(this.getFMPrefix() + ".tenderTitle")
                && <Item label="Tender Title" value={tenderTitle} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".contractor")
                && <Item label="Contractor" value={i.contract.awardee.label} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".fiscalYear")
                && <Item label="Fiscal Year" value={fiscalYear.name} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".approvedDate")
                && <Item label="Report Date" value={formatDate(i.approvedDate)} col={3} />}
              </div>
                {
                  this.childElements(i)
                }
                {
                  i.formDocs && isFeatureVisible(this.getFMPrefix() + ".formDocs") ?
                      <div className="row">
                        <Item label={this.getReportName() + " Uploads"} col={12}>
                          <FileDownloadLinks files={i.formDocs} />
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
