import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";

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
        {isFeatureVisible("publicView.tenderProcess.purchaseRequestNumber")
        && <Item label="Purchase Request Number" value={data.purchaseRequestNumber} col={3} />}
      </div>

      {
        data.purchRequisitions !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions")
          ?
          data.purchRequisitions.map(preq => <div key={preq._id} className="box">
            <div className="row">
              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestedBy")
              && <Item label="Requested By" value={preq.requestedBy.label} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.chargeAccount")
              && <Item label="Charge Account" value={preq.chargeAccount.label} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestApprovalDate")
              && <Item label="Request Approval Date" value={formatDate(preq.requestApprovalDate)} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.approvedDate")
              && <Item label="Approved Date" value={formatDate(preq.approvedDate)} col={3} />}
            </div>
            {
              preq.purchaseItems !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems")
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">Purchase Requisition Items
                      ({preq.purchaseItems.length})
                    </div>
                  </div>
                  {
                    preq.purchaseItems.map(pr => <div key={pr._id} className="box">
                      <div className="row display-flex">
                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.label")
                        && <Item label="Item" value={pr.planItem.item.label} col={6} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.description")
                        && <Item label="Description" value={pr.description} col={6} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.unitOfIssue")
                        && <Item label="Unit of Issue" value={pr.planItem.unitOfIssue.label} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.quantity")
                        && <Item label="Quantity" value={currencyFormatter(pr.quantity)} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.amount")
                        && <Item label="Unit Price" value={currencyFormatter(pr.amount)} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.totalCost")
                        && <Item label="Total Cost" value={currencyFormatter(pr.quantity * pr.amount)} col={3} />}
                      </div>
                    </div>)
                  }
                </div>
                : null
            }
              <div className="row">
                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.formDocs")
                && <Item label="Purchase Requisition Documents" col={6}>
                  <FileDownloadLinks files={preq.formDocs} useDash />
                </Item>}
              </div>
          </div>
          )
          : null
      }

    </div>);
  }
}

export default fmConnect(PurchaseReq);
