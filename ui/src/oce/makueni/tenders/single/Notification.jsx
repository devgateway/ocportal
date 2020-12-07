import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import translatable from "../../../translatable";

class Notification extends translatable(React.Component) {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape(this.t("notification:subject") + metadata);
  }

  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const awardNotification = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardNotification.items !== undefined
            ? awardNotification.items.map(i => <div key={i._id} className="box">
              <div className="row padding-top-10">
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:awardValue")}</div>
                  <div className="item-value">{currencyFormatter(i.awardValue)}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:awardDate")}</div>
                  <div className="item-value">{formatDate(i.awardDate)}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:acknowledgementDays")}</div>
                  <div className="item-value">{i.acknowledgementDays}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:awardee:label")}</div>
                  <div className="item-value">{i.awardee.label}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:awardee:code")}</div>
                  <div className="item-value">{i.awardee.code}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">{this.t("notification:awardee:address")}</div>
                  <div className="item-value">{i.awardee.address}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-12">
                  <div className="item-label">{this.t("notification:docs")}</div>

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

            </div>
            ) : null
        }
      </div>
    </div>);
  }
}

export default Notification;
