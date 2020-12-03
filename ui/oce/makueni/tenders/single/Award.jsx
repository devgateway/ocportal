import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
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
    const { data, isFeatureVisible } = this.props;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const awardAcceptance = data[0];

    return (<div>
      <div className="padding-top-10">
        {
          awardAcceptance.items !== undefined && isFeatureVisible("publicView.awardAcceptance.items")
            ? awardAcceptance.items.map(i => <div key={i._id} className="box">

              <div className="row">
                {isFeatureVisible("publicView.awardAcceptance.items.supplierResponse")
                && <Item label={this.t("award:supplierResponse")} value={i.supplierResponse.label} col={3} />}

                {isFeatureVisible("publicView.awardAcceptance.items.acceptedAwardValue")
                && <Item label={this.t("award:acceptedAwardValue")} value={currencyFormatter(i.acceptedAwardValue)} col={3} />}

                {isFeatureVisible("publicView.awardAcceptance.items.acceptanceDate")
                && <Item label={this.t("award:acceptanceDate")} value={formatDate(i.acceptanceDate)} col={3} />}

                {isFeatureVisible("publicView.awardAcceptance.items.awardee.label")
                && <Item label={this.t("award:awardeeLabel")} value={i.awardee.label} col={3} />}

                {isFeatureVisible("publicView.awardAcceptance.items.awardee.code")
                && <Item label={this.t("award:awardeeCode")} value={i.awardee.code} col={3} />}

                {isFeatureVisible("publicView.awardAcceptance.items.formDocs")
                && <Item label={this.t("award:letterOfAcceptanceOfAward")} col={12}>
                  <FileDownloadLinks files={i.formDocs} useDash />
                </Item>}
              </div>
            </div>) : null
        }
      </div>
    </div>);
  }
}

export default fmConnect(Award);
