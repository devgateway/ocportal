import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

class Award extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape('Award' + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const awardAcceptance = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardAcceptance.items !== undefined && isFeatureVisible("awardAcceptanceForm.items")
            ? awardAcceptance.items.map(i => <div key={i._id} className="box">

              {isFeatureVisible("awardAcceptanceForm.items.supplierResponse")
              && <div className="row">
                <Item label="Supplier Response" value={i.supplierResponse.label} col={3} />
              </div>}

              <div className="row">
                {isFeatureVisible("awardAcceptanceForm.items.acceptedAwardValue")
                && <Item label="Accepted Award Value" value={currencyFormatter(i.acceptedAwardValue)} col={3} />}

                {isFeatureVisible("awardAcceptanceForm.items.acceptanceDate")
                && <Item label="Response Date" value={formatDate(i.acceptanceDate)} col={3} />}

                {isFeatureVisible("awardAcceptanceForm.items.awardee")
                && <React.Fragment>
                  <Item label="Supplier Name" value={i.awardee.label} col={3} />
                  <Item label="Supplier ID" value={i.awardee.code} col={3} />
                </React.Fragment>}
              </div>

              <div className="row">
                {isFeatureVisible("awardAcceptanceForm.items.formDocs")
                && <Item label="Letter of Acceptance of Award" col={12}>
                  <FileDownloadLinks files={i.formDocs} />
                </Item>}
              </div>
            </div>) : null
        }
      </div>
    </div>);
  }
}

export default fmConnect(Award);
