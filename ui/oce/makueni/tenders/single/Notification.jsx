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
      <div className="padding-top-10">
        {
          awardNotification.items !== undefined && isFeatureVisible("publicView.awardNotification.items")
            ? awardNotification.items.map(i => <div key={i._id} className="box">
              <div className="row">
                {isFeatureVisible("publicView.awardNotification.items.awardValue")
                && <Item label="Award Value" value={currencyFormatter(i.awardValue)} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardDate")
                && <Item label="Date" value={formatDate(i.awardDate)} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.acknowledgementDays")
                && <Item label="Acknowledge Receipt of Award Timeline" value={i.acknowledgementDays} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.label")
                && <Item label="Supplier Name" value={i.awardee.label} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.code")
                && <Item label="Winning Bid Supplier ID" value={i.awardee.code} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.awardee.address")
                && <Item label="Supplier Postal Address" value={i.awardee.address} col={4} />}

                {isFeatureVisible("publicView.awardNotification.items.formDocs")
                && <Item label="Letter of Notification of Award" col={12}>
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
