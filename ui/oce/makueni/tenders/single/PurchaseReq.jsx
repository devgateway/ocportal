import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class PurchaseReq extends React.Component {
  getFeedbackSubject() {
    const { data, department, fiscalYear } = this.props;

    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.title
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Tender Process" + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    return (<div>
      <div className="row">
        {isFeatureVisible("tenderProcessForm.purchaseRequestNumber")
        && <Item label="Purchase Request Number" value={data.purchaseRequestNumber}
                 col={3} />}
      </div>

      {
        data.purchRequisitions !== undefined && isFeatureVisible("tenderProcessForm.purchRequisitions")
          ?
          data.purchRequisitions.map(preq => <div key={preq._id} className="box">
            <div className="row">
              {isFeatureVisible("tenderProcessForm.purchRequisitions.requestedBy")
              && <Item label="Requested By" value={preq.requestedBy.label}
                       col={3} />}

              {isFeatureVisible("tenderProcessForm.purchRequisitions.chargeAccount")
              && <Item label="Charge Account" value={preq.chargeAccount.label}
                       col={3} />}

              {isFeatureVisible("tenderProcessForm.purchRequisitions.requestApprovalDate")
              && <Item label="Request Approval Date" value={formatDate(preq.requestApprovalDate)}
                       col={3} />}

              {isFeatureVisible("tenderProcessForm.purchRequisitions.approvedDate")
              && <Item label="Approved Date" value={formatDate(preq.approvedDate)}
                       col={3} />}
            </div>
            {
              preq.purchaseItems !== undefined && isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems")
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">Purchase Requisition Items
                      ({preq.purchaseItems.length})
                    </div>
                  </div>
                  {
                    preq.purchaseItems.map(pr => <div key={pr._id} className="box">
                      <div className="row">
                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.planItem")
                        && <Item label="Item" value={pr.planItem.item.label} col={6} />}

                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.description")
                        && <Item label="Description" value={pr.description} col={6} />}
                      </div>
                      <div className="row">
                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.planItem")
                        && <Item label="Unit of Issue" value={pr.planItem.unitOfIssue.label} col={3} />}

                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.quantity")
                        && <Item label="Quantity" value={currencyFormatter(pr.quantity)} col={3} />}

                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.amount")
                        && <Item label="Unit Price" value={currencyFormatter(pr.amount)} col={3} />}

                        {isFeatureVisible("tenderProcessForm.purchRequisitions.purchaseItems.totalCost")
                        && <Item label="Total Cost" value={currencyFormatter(pr.quantity * pr.amount)} col={3} />}
                      </div>
                    </div>)
                  }
                </div>
                : null
            }
              <div className="row">
                {isFeatureVisible("tenderProcessForm.purchRequisitions.formDocs")
                && <Item label="Purchase Requisition Documents" col={6}>
                  {
                    preq.formDocs.map(doc => <div key={doc._id}>
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

                {isFeatureVisible("tenderProcessForm.purchRequisitions.approvedDate")
                && <Item label="Approved Date"
                         value={formatDate(preq.approvedDate)}
                         col={6} />}
              </div>
          </div>
          )
          : null
      }

    </div>);
  }
}

export default fmConnect(PurchaseReq);
