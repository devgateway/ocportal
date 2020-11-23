import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

class Contract extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Contract" + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const contract = data[0];

    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.contract.referenceNumber")
        && <Item label="Reference Number" value={contract.referenceNumber} col={4} />}

        {isFeatureVisible("publicView.contract.contractDate")
        && <Item label="Contract Date" value={formatDate(contract.contractDate)} col={4} />}

        {isFeatureVisible("publicView.contract.expiryDate")
        && <Item label="Expiry Date" value={formatDate(contract.expiryDate)} col={4} />}

        {isFeatureVisible("publicView.contract.awardee.label")
        && <Item label="Supplier Name" value={contract.awardee.label} col={6} />}

        {isFeatureVisible("publicView.contract.awardee.address")
        && <Item label="Supplier Postal Address" value={contract.awardee.address} col={6} />}

        {isFeatureVisible("publicView.contract.procuringEntity")
        && <Item label="Procuring Entity Name" value={contract.procuringEntity.label} col={4} />}

        {isFeatureVisible("publicView.contract.contractValue")
        && <Item label="Contract Value" value={currencyFormatter(contract.contractValue)} col={4} />}

        {isFeatureVisible("publicView.contract.contractApprovalDate")
        && <Item label="Contract Approved Date" value={formatDate(contract.contractApprovalDate)} col={4} />}
      </div>

      {
        contract.contractDocs !== undefined && isFeatureVisible("publicView.contract.contractDocs")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Contract Documents
                ({contract.contractDocs.length})
              </div>
            </div>

            {
              contract.contractDocs.map(contractDoc => <div key={contractDoc._id} className="box">
                <div className="row">
                  {isFeatureVisible("publicView.contract.contractDocs.contractDocumentType")
                  && <Item label="Contract Document Type" value={contractDoc.contractDocumentType.label} col={6} />}

                  {isFeatureVisible("publicView.contract.contractDocs.formDocs")
                  && <Item label="Contract Documents" col={6}>
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
