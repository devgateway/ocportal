import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

class Notification extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Notification" + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const awardNotification = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardNotification.items !== undefined && isFeatureVisible("awardNotificationForm.items")
            ? awardNotification.items.map(i => <div key={i._id} className="box">
              <div className="row">
                {isFeatureVisible("awardNotificationForm.items.awardValue")
                && <Item label="Award Value" value={currencyFormatter(i.awardValue)} col={4} />}

                {isFeatureVisible("awardNotificationForm.items.awardDate")
                && <Item label="Date" value={formatDate(i.awardDate)} col={4} />}

                {isFeatureVisible("awardNotificationForm.items.acknowledgementDays")
                && <Item label="Acknowledge Receipt of Award Timeline" value={i.acknowledgementDays} col={4} />}
              </div>

              {isFeatureVisible("awardNotificationForm.items.awardee")
              && <div className="row">

                <Item label="Supplier Name" value={i.awardee.label} col={4} />

                <Item label="Winning Bid Supplier ID" value={i.awardee.code} col={4} />

                <Item label="Supplier Postal Address" value={i.awardee.address} col={4} />
              </div>}

              {isFeatureVisible("awardNotificationForm.items.formDocs")
              && <div className="row">

                <Item label="Letter of Notification of Award" col={12}>
                  <FileDownloadLinks files={i.formDocs} />
                </Item>
              </div>}

            </div>
            ) : null
        }
      </div>
    </div>);
  }
}

export default fmConnect(Notification);
