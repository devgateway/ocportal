import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import {tCreator} from "../../../translatable";
import defaultSingleTenderTabTypes from "./singleUtil";

const Contract = (props) => {

  const t = tCreator(props.translations);
  const {data, isFeatureVisible} = props;
  const {currencyFormatter, formatDate} = props.styling.tables;

  const getFeedbackSubject = () => {
    const {tenderTitle, department, fiscalYear} = props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
          + " - " + department.label
          + " - " + fiscalYear.name;
    }
    return escape(t("contract:subject") + metadata);
  }

  const getContract = (contract) => {
    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.contract.referenceNumber")
        && <Item label={t("contract:referenceNumber")} value={contract.referenceNumber} col={4}/>}

        {isFeatureVisible("publicView.contract.contractDate")
        && <Item label={t("contract:contractDate")} value={formatDate(contract.contractDate)} col={4}/>}

        {isFeatureVisible("publicView.contract.expiryDate")
        && <Item label={t("contract:expiryDate")} value={formatDate(contract.expiryDate)} col={4}/>}

        {isFeatureVisible("publicView.contract.awardee.label")
        && <Item label={t("contract:awardee:label")} value={contract.awardee.label} col={6}/>}

        {isFeatureVisible("publicView.contract.awardee.address")
        && <Item label={t("contract:awardee:address")} value={contract.awardee.address} col={6}/>}

        {isFeatureVisible("publicView.contract.procuringEntity")
        && <Item label={t("contract:procuringEntity:label")} value={contract.procuringEntity.label} col={4}/>}

        {isFeatureVisible("publicView.contract.contractValue")
        && <Item label={t("contract:contractValue")} value={currencyFormatter(contract.contractValue)} col={4}/>}

        {isFeatureVisible("publicView.contract.contractApprovalDate")
        &&
        <Item label={t("contract:contractApprovalDate")} value={formatDate(contract.contractApprovalDate)} col={4}/>}
      </div>

      {
        contract.contractDocs !== undefined && isFeatureVisible("publicView.contract.contractDocs")
            ? <div>
              <div className="row padding-top-10">
                <div className="col-md-12 sub-title">{t("contract:contractDocs")}
                  ({contract.contractDocs.length})
                </div>
              </div>

              {
                contract.contractDocs.map(contractDoc => <div key={contractDoc._id} className="box">
                  <div className="row">
                    {isFeatureVisible("publicView.contract.contractDocs.contractDocumentType")
                    && <Item label={t("contract:contractDocs:type")} value={contractDoc.contractDocumentType.label}
                             col={6}/>}

                    {isFeatureVisible("publicView.contract.contractDocs.formDocs")
                    && <Item label={t("contract:contractDocs")} col={6}>
                      <FileDownloadLinks files={contractDoc.formDocs} useDash/>
                    </Item>}
                  </div>
                </div>)
              }
            </div>
            : null
      }
    </div>);
  }

  return (data === undefined ? <NoDataMessage translations={props.translations}/> : getContract(data[0]));
}


Contract.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(Contract);
