import NoDataMessage from './NoData';
import React from 'react';
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class ImplReport extends translatable(React.Component) {
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
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    return (<div>
      <div className="padding-top-10">
        {
          data.sort((a, b) => new Date(a.approvedDate) - new Date(b.approvedDate))
              .map(i => <div key={i._id} className="box">
              <div className="row">
                {isFeatureVisible(this.getFMPrefix() + ".tenderTitle")
                && <Item label={this.t("report:tenderTitle")} value={tenderTitle} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".contractor")
                && <Item label={this.t("report:awardee:label")} value={i.contract.awardee.label} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".fiscalYear")
                && <Item label={this.t("report:fiscalYear")} value={fiscalYear.name} col={3} />}

                {isFeatureVisible(this.getFMPrefix() + ".approvedDate")
                && <Item label={this.t("report:approvedDate")} value={formatDate(i.approvedDate)} col={3} />}
              </div>
                {
                  this.childElements(i)
                }
                {
                  i.formDocs && isFeatureVisible(this.getFMPrefix() + ".formDocs") ?
                      <div className="row">
                        <Item label={this.t("report:uploads").replace("$#$", this.getReportName())} col={12}>
                          <FileDownloadLinks files={i.formDocs} useDash />
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
