import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
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
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const awardNotification = data[0];

    return (<div>
      <div className="padding-top-10">
        {
          awardNotification.items !== undefined && isFeatureVisible("publicView.awardNotification.items")
            ? awardNotification.items.map(i => <div key={i._id} className="box">
              <div className="row">
                {isFeatureVisible("publicView.awardNotification.items.awardValue")
                && <Item label={this.t("notification:awardValue")} value={currencyFormatter(i.awardValue)} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardDate")
                && <Item label={this.t("notification:awardDate")} value={formatDate(i.awardDate)} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.acknowledgementDays")
                && <Item label={this.t("notification:acknowledgementDays")} value={i.acknowledgementDays} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.label")
                && <Item label={this.t("notification:awardee:label")} value={i.awardee.label} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.code")
                && <Item label={this.t("notification:awardee:code")} value={i.awardee.code} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.address")
                && <Item label={this.t("notification:awardee:address")} value={i.awardee.address} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.formDocs")
                && <Item label={this.t("notification:docs")} col={12}>
                  <FileDownloadLinks files={i.formDocs} useDash />
                </Item>}
              </div>

            </div>
            ) : null
        }
      </div>
    </div>);
  }
}

export default fmConnect(Notification);
