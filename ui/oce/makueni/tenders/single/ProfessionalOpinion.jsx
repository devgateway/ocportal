import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class ProfessionalOpinion extends translatable(React.Component) {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape(this.t("professionalOpinion:label") + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const professionalOpinion = data[0];

    return (<div>
      <div className="padding-top-10">
      {
        professionalOpinion.items !== undefined && isFeatureVisible("publicView.professionalOpinions.items")
          ? professionalOpinion.items.map(i => <div key={i._id} className="box">
            <div className="row">
              {isFeatureVisible("publicView.professionalOpinions.items.professionalOpinionDate")
              && <Item label={this.t("professionalOpinion:professionalOpinionDate")}
                       value={formatDate(i.professionalOpinionDate)} col={4} />}
              {isFeatureVisible("publicView.professionalOpinions.items.awardee")
              && <Item label={this.t("professionalOpinion:awardee")} value={i.awardee.label} col={4} />}
              {isFeatureVisible("publicView.professionalOpinions.items.recommendedAwardAmount")
              && <Item label={this.t("professionalOpinion:recommendedAwardAmount")}
                       value={currencyFormatter(i.recommendedAwardAmount)} col={4} />}
              {isFeatureVisible("publicView.professionalOpinions.items.formDocs")
              && <Item label={this.t("professionalOpinion:docs")} col={4}>
                <FileDownloadLinks files={i.formDocs} useDash />
              </Item>}
              {isFeatureVisible("publicView.professionalOpinions.items.approvedDate")
              && <Item label={this.t("professionalOpinion:approvedDate")}
                       value={formatDate(i.approvedDate)} col={4} />}
            </div>
          </div>
          ) : null
      }
      </div>
    </div>);
  }
}

export default fmConnect(ProfessionalOpinion);
