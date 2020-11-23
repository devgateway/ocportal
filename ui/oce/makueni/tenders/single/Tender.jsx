import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

class Tender extends React.Component {
  getFeedbackSubject() {
    const { data, department, fiscalYear } = this.props;

    let metadata;
    if (data !== undefined) {
      const tender = data[0];

      metadata = ' - ' + tender.tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape('Tender' + metadata);
  }

  render() {
    const { data, prId, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const tender = data[0];

    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.tender.tenderTitle")
        && <Item label={"Tender Name"} value={tender.tenderTitle} col={4} />}

        {isFeatureVisible("publicView.tender.tenderNumber")
        && <Item label={"Tender ID"} value={tender.tenderNumber} col={4} />}

        {isFeatureVisible("publicView.tender.tenderCode")
        && <Item label={"Tender Code"} value={prId} col={4} />}

        {isFeatureVisible("publicView.tender.procurementMethod")
        && <Item label={"Procurement Method"} value={tender.procurementMethod.label} col={4} />}

        {isFeatureVisible("publicView.tender.procurementMethodRationale")
        && <Item label={"Procurement Method Rationale"}
                 value={(tender.procurementMethodRationale || {}).label} col={4} />}

        {isFeatureVisible("publicView.tender.invitationDate")
        && <Item label={"Invitation to Tender Date"} value={formatDate(tender.invitationDate)} col={4} />}

        {isFeatureVisible("publicView.tender.closingDate")
        && <Item label={"Closing Date"} value={formatDate(tender.closingDate)} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.label")
        && <Item label={"Tender Issued By"} value={tender.issuedBy.label} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.address")
        && <Item label={"Procuring Entity Address"} value={tender.issuedBy.address} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.emailAddress")
        && <Item label={"Procuring Entity Email"} col={4}>
          {tender.issuedBy.emailAddress
            ? <a className="download" href={"mailto:"+tender.issuedBy.emailAddress}>
              {tender.issuedBy.emailAddress}
            </a>
            : "-"
          }
        </Item>}

        {isFeatureVisible("publicView.tender.tenderValue")
        && <Item label={"Tender Value"} value={currencyFormatter(tender.tenderValue)} col={4} />}

        {isFeatureVisible("publicView.tender.targetGroup")
        && <Item label={"Target Group"} value={tender.targetGroup && tender.targetGroup.label} col={4} />}

        {isFeatureVisible("publicView.tender.objective")
        && <Item label={"Tender Objective"} value={tender.objective} col={12} />}
      </div>

      {
        tender.tenderItems !== undefined && isFeatureVisible("publicView.tender.tenderItems")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Tender Items
                ({tender.tenderItems.length})
              </div>
            </div>

            {
              tender.tenderItems.map(tenderItem => <div key={tenderItem._id} className="box">
                <div className="row display-flex">
                  {isFeatureVisible("publicView.tender.tenderItems.item")
                  && <Item label={"Item"} value={tenderItem.purchaseItem.planItem.item.label} col={6} />}

                  {isFeatureVisible("publicView.tender.tenderItems.description")
                  && <Item label={"Description"} value={tenderItem.description} col={6} />}

                  {isFeatureVisible("publicView.tender.tenderItems.purchaseItem")
                  && <Item label={"Unit of Issue"} value={tenderItem.purchaseItem.planItem.unitOfIssue.label}
                           col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.quantity")
                  && <Item label={"Quantity"} value={currencyFormatter(tenderItem.quantity)} col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.unitPrice")
                  && <Item label={"Unit Price"} value={currencyFormatter(tenderItem.unitPrice)} col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.totalCost")
                  && <Item label={"Total Cost"}
                           value={currencyFormatter(tenderItem.quantity * tenderItem.unitPrice)} col={3} />}
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row">
        {isFeatureVisible("publicView.tender.formDocs")
        && <Item label="Download Tender" col={6}
                 data-intro="Download the original hardcopy of the tender document or link to a site
                 where the document can be downloaded.">
          <FileDownloadLinks files={tender.formDocs || []} />
        </Item>}

        {isFeatureVisible("publicView.tender.tenderLink")
        && <Item label={"Tender Link"} col={6}>
          {tender.tenderLink
            ? <a className="download" href={tender.tenderLink} target="_blank">
              {tender.tenderLink}
            </a>
            : "-"
          }
        </Item>}
      </div>
    </div>);
  }
}

export default fmConnect(Tender);
