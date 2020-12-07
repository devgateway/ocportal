import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
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
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const professionalOpinion = data[0];

    return (<div>
      <div className="row padding-top-10">
      {
        professionalOpinion.items !== undefined
          ? professionalOpinion.items.map(i => <div key={i._id} className="box">
            <div className="row padding-top-10">
              <div className="col-md-4">
                <div className="item-label">{this.t("professionalOpinion:professionalOpinionDate")}</div>
                <div
                  className="item-value">{formatDate(i.professionalOpinionDate)}</div>
              </div>
              <div className="col-md-4">
                <div className="item-label">{this.t("professionalOpinion:awardee")}</div>
                <div className="item-value">{i.awardee.label}</div>
              </div>
              <div className="col-md-4">
                <div className="item-label">{this.t("professionalOpinion:recommendedAwardAmount")}</div>
                <div
                  className="item-value">{currencyFormatter(i.recommendedAwardAmount)}</div>
              </div>
            </div>

            <div className="row padding-top-10">
              <div className="col-md-6">
                <div className="item-label">{this.t("professionalOpinion:docs")}</div>

                {
                  i.formDocs.map(doc => <div key={doc._id}>
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
              <div className="col-md-6">
                <div className="item-label">{this.t("professionalOpinion:approvedDate")}</div>
                <div
                  className="item-value">{formatDate(i.approvedDate)}</div>
              </div>
            </div>
          </div>
          ) : null
      }
      </div>
    </div>);
  }
}

export default ProfessionalOpinion;
