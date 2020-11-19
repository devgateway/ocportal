import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

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
      <div className="row">
        {isFeatureVisible("contractForm.referenceNumber")
        && <Item label="Reference Number" value={contract.referenceNumber} col={4} />}

        {isFeatureVisible("contractForm.contractDate")
        && <Item label="Contract Date" value={formatDate(contract.contractDate)} col={4} />}

        {isFeatureVisible("contractForm.expiryDate")
        && <Item label="Expiry Date" value={formatDate(contract.expiryDate)} col={4} />}
      </div>

      {isFeatureVisible("contractForm.awardee")
      && <div className="row">
        <Item label="Supplier Name" value={contract.awardee.label} col={6} />
        <Item label="Supplier Postal Address" value={contract.awardee.address} col={6} />
      </div>}

      <div className="row">
        {isFeatureVisible("contractForm.procuringEntity")
        && <Item label="Procuring Entity Name" value={contract.procuringEntity.label} col={4} />}

        {isFeatureVisible("contractForm.contractValue")
        && <Item label="Contract Value" value={currencyFormatter(contract.contractValue)} col={4} />}

        {isFeatureVisible("contractForm.contractApprovalDate")
        && <Item label="Contract Approved Date" value={formatDate(contract.contractApprovalDate)} col={4} />}
      </div>

      {
        contract.contractDocs !== undefined && isFeatureVisible("contractForm.contractDocs")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Contract Documents
                ({contract.contractDocs.length})
              </div>
            </div>

            {
              contract.contractDocs.map(contractDoc => <div key={contractDoc._id} className="box">
                <div className="row">
                  {isFeatureVisible("contractForm.contractDocs.contractDocumentType")
                  && <Item label="Contract Document Type" value={contractDoc.contractDocumentType.label} col={6} />}

                  {isFeatureVisible("contractForm.contractDocs.formDocs")
                  && <Item label="Contract Documents" col={6}>
                    {
                      contractDoc.formDocs.map(doc => <div key={doc._id}>
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
