import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

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
              <div className="row padding-top-10">
                {isFeatureVisible("awardNotificationForm.items.awardValue")
                && <Item label="Award Value" value={currencyFormatter(i.awardValue)} col={4} />}

                {isFeatureVisible("awardNotificationForm.items.awardDate")
                && <Item label="Date" value={formatDate(i.awardDate)} col={4} />}

                {isFeatureVisible("awardNotificationForm.items.acknowledgementDays")
                && <Item label="Acknowledge Receipt of Award Timeline" value={i.acknowledgementDays} col={4} />}
              </div>

              {isFeatureVisible("awardNotificationForm.items.awardee")
              && <div className="row padding-top-10">

                <Item label="Supplier Name" value={i.awardee.label} col={4} />

                <Item label="Winning Bid Supplier ID" value={i.awardee.code} col={4} />

                <Item label="Supplier Postal Address" value={i.awardee.address} col={4} />
              </div>}

              {isFeatureVisible("awardNotificationForm.items.formDocs")
              && <div className="row padding-top-10">

                <Item label="Letter of Notification of Award" col={12}>
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
              </div>}

            </div>
            ) : null
        }
      </div>
    </div>);
  }
}

export default fmConnect(Notification);
