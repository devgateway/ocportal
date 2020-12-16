import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import translatable from "../../../translatable";

class Award extends translatable(React.Component) {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape(this.t("award:subject") + metadata);
  }

  render() {
    const { data } = this.props;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const awardAcceptance = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardAcceptance.items !== undefined
            ? awardAcceptance.items.map(i => <div key={i._id} className="box">
              <div className="row padding-top-10">
                <div className="col-md-3">
                  <div className="item-label">{this.t("award:supplierResponse")}</div>
                  <div className="item-value">{i.supplierResponse.label}</div>
                </div>
              </div>
              <div className="row padding-top-10">
                <div className="col-md-3">
                  <div className="item-label">{this.t("award:acceptedAwardValue")}</div>
                  <div className="item-value">{currencyFormatter(i.acceptedAwardValue)}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">{this.t("award:acceptanceDate")}</div>
                  <div className="item-value">{formatDate(i.acceptanceDate)}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">{this.t("award:awardeeLabel")}</div>
                  <div className="item-value">{i.awardee.label}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">{this.t("award:awardeeCode")}</div>
                  <div className="item-value">{i.awardee.code}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-12">
                  <div className="item-label">{this.t("award:letterOfAcceptanceOfAward")}</div>

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
              </div>
            </div>) : null
        }
      </div>
    </div>);
  }
}

export default Award;
