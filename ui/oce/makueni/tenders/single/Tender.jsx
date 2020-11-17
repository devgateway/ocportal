import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";

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

    const item = (label, value, width = 4) =>
      <div className={"padding-top-10 col-md-" + width}>
        <div className="item-label">{label}</div>
        <div className="item-value">{value}</div>
      </div>

    return (<div>
      <div className="row">
        {item("Tender Name", tender.tenderTitle)}

        {isFeatureVisible("tenderForm.tenderNumber")
        && item("Tender ID", tender.tenderNumber)}

        {item("Tender Code", prId)}
      </div>

      <div className="row">

        {isFeatureVisible("tenderForm.procurementMethod")
        && item("Procurement Method", tender.procurementMethod.label)}

        {isFeatureVisible("tenderForm.procurementMethodRationale")
        && item("Procurement Method Rationale",
          tender.procurementMethodRationale && tender.procurementMethodRationale.label)}

        {isFeatureVisible("tenderForm.invitationDate")
        && item("Invitation to Tender Date", formatDate(tender.invitationDate))}
      </div>
      <div className="row">

        {isFeatureVisible("tenderForm.closingDate")
        && item("Closing Date", formatDate(tender.closingDate))}

        {isFeatureVisible("tenderForm.issuedBy")
        && item("Tender Issued By", tender.issuedBy.label)}

        {isFeatureVisible("tenderForm.issuedBy")
        && item("Procuring Entity Address", tender.issuedBy.address)}
      </div>

      <div className="row">

        {isFeatureVisible("tenderForm.issuedBy")
        &&
        <div className="padding-top-10 col-md-4">
          <div className="item-label">Procuring Entity Email</div>
          <div className="item-value download">
            <a href={"mailto:"+tender.issuedBy.emailAddress}>{tender.issuedBy.emailAddress}</a></div>
        </div>}

        {isFeatureVisible("tenderForm.tenderValue")
        && item("Tender Value", currencyFormatter(tender.tenderValue))}

        {isFeatureVisible("tenderForm.targetGroup")
        && item("Target Group", tender.targetGroup && tender.targetGroup.label)}
      </div>

      <div className="row">

        {isFeatureVisible("tenderForm.objective")
        && item("Tender Objective", tender.objective, 12)}
      </div>

      {
        tender.tenderItems !== undefined && isFeatureVisible("tenderForm.tenderItems")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Tender Items
                ({tender.tenderItems.length})
              </div>
            </div>

            {
              tender.tenderItems.map(tenderItem => <div key={tenderItem._id} className="box">
                <div className="row">
                  {item("Item", tenderItem.purchaseItem.planItem.item.label, 6)}

                  {isFeatureVisible("tenderForm.tenderItems.description")
                  && item("Description", tenderItem.description, 6)}
                </div>
                <div className="row">
                  {isFeatureVisible("tenderForm.tenderItems.purchaseItem")
                  && item("Unit of Issue", tenderItem.purchaseItem.planItem.unitOfIssue.label, 3)}

                  {isFeatureVisible("tenderForm.tenderItems.quantity")
                  && item("Quantity", currencyFormatter(tenderItem.quantity), 3)}

                  {isFeatureVisible("tenderForm.tenderItems.unitPrice")
                  && item("Unit Price", currencyFormatter(tenderItem.unitPrice), 3)}

                  {isFeatureVisible("tenderForm.tenderItems.totalCost")
                  && item("Total Cost", currencyFormatter(tenderItem.quantity * tenderItem.unitPrice), 3)}
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row">
        {isFeatureVisible("tenderForm.formDocs")
        && <div className="col-md-6 padding-top-10"
             data-intro="Download the original hardcopy of the tender document or link to a site
             where the document can be downloaded.">
          <div className="item-label">Download Tender</div>
          {
            tender.formDocs && tender.formDocs.map(doc => <div key={doc._id}>
              <OverlayTrigger
                placement="bottom"
                overlay={
                  <Tooltip id="download-tooltip">
                    Click to download the file
                  </Tooltip>
                }>

                <a className="item-value download" href={doc.url} target="_blank">
                  <i className="glyphicon glyphicon-download"/>
                  <span>{doc.name}</span>
                </a>
              </OverlayTrigger>
            </div>)
          }
        </div>}
        {isFeatureVisible("tenderForm.tenderLink")
        && <div className="col-md-6 padding-top-10">
          <div className="item-label">Tender Link</div>
          <div className="item-value">
            <a className="item-value download" href={tender.tenderLink} target="_blank">
              {tender.tenderLink}
            </a>

          </div>
        </div>}
      </div>
    </div>);
  }
}

export default fmConnect(Tender);
