import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class Contract extends translatable(React.Component) {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape(this.t("contract:subject") + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const contract = data[0];

    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.contract.referenceNumber")
        && <Item label={this.t("contract:referenceNumber")} value={contract.referenceNumber} col={4} />}

        {isFeatureVisible("publicView.contract.contractDate")
        && <Item label={this.t("contract:contractDate")} value={formatDate(contract.contractDate)} col={4} />}

        {isFeatureVisible("publicView.contract.expiryDate")
        && <Item label={this.t("contract:expiryDate")} value={formatDate(contract.expiryDate)} col={4} />}

        {isFeatureVisible("publicView.contract.awardee.label")
        && <Item label={this.t("contract:awardee:label")} value={contract.awardee.label} col={6} />}

        {isFeatureVisible("publicView.contract.awardee.address")
        && <Item label={this.t("contract:awardee:address")} value={contract.awardee.address} col={6} />}

        {isFeatureVisible("publicView.contract.procuringEntity")
        && <Item label={this.t("contract:procuringEntity:label")} value={contract.procuringEntity.label} col={4} />}

        {isFeatureVisible("publicView.contract.contractValue")
        && <Item label={this.t("contract:contractValue")} value={currencyFormatter(contract.contractValue)} col={4} />}

        {isFeatureVisible("publicView.contract.contractApprovalDate")
        && <Item label={this.t("contract:contractApprovalDate")} value={formatDate(contract.contractApprovalDate)} col={4} />}
      </div>

      {
        contract.contractDocs !== undefined && isFeatureVisible("publicView.contract.contractDocs")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("contract:contractDocs")}
                ({contract.contractDocs.length})
              </div>
            </div>

            {
              contract.contractDocs.map(contractDoc => <div key={contractDoc._id} className="box">
                <div className="row">
                  {isFeatureVisible("publicView.contract.contractDocs.contractDocumentType")
                  && <Item label={this.t("contract:contractDocs:type")} value={contractDoc.contractDocumentType.label} col={6} />}

                  {isFeatureVisible("publicView.contract.contractDocs.formDocs")
                  && <Item label={this.t("contract:contractDocs")} col={6}>
                    <FileDownloadLinks files={contractDoc.formDocs} useDash />
                  </Item>}
                </div>
              </div>)
            }
          </div>
          : null
      }
    </div>);
  }
}

export default fmConnect(Contract);
