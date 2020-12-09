import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class PurchaseReq extends translatable(React.Component) {
  getFeedbackSubject() {
    const { data, department, fiscalYear } = this.props;

    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.title
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape(this.t("purchaseReq:label") + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    return (<div>
      <div className="row">
        {isFeatureVisible("publicView.tenderProcess.purchaseRequestNumber")
        && <Item label={this.t("purchaseReq:purchaseRequestNumber")} value={data.purchaseRequestNumber} col={3} />}
      </div>

      {
        data.purchRequisitions !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions")
          ?
          data.purchRequisitions.map(preq => <div key={preq._id} className="box">
            <div className="row">
              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestedBy")
              && <Item label={this.t("purchaseReq:requestedBy")} value={preq.requestedBy.label} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.chargeAccount")
              && <Item label={this.t("purchaseReq:chargeAccount")} value={preq.chargeAccount.label} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestApprovalDate")
              && <Item label={this.t("purchaseReq:requestApprovalDate")}
                       value={formatDate(preq.requestApprovalDate)} col={3} />}

              {isFeatureVisible("publicView.tenderProcess.purchRequisitions.approvedDate")
              && <Item label={this.t("purchaseReq:approvedDate")} value={formatDate(preq.approvedDate)} col={3} />}
            </div>
            {
              preq.purchaseItems !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems")
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">{this.t("purchaseReq:items")}
                      ({preq.purchaseItems.length})
                    </div>
                  </div>
                  {
                    preq.purchaseItems.map(pr => <div key={pr._id} className="box">
                      <div className="row display-flex">
                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.label")
                        && <Item label={this.t("purchaseReq:items:item")} value={pr.planItem.item.label} col={6} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.description")
                        && <Item label={this.t("purchaseReq:items:description")} value={pr.description} col={6} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.unitOfIssue")
                        && <Item label={this.t("purchaseReq:items:unitOfIssue")} value={pr.planItem.unitOfIssue.label} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.quantity")
                        && <Item label={this.t("purchaseReq:items:quantity")} value={currencyFormatter(pr.quantity)} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.amount")
                        && <Item label={this.t("purchaseReq:items:amount")} value={currencyFormatter(pr.amount)} col={3} />}

                        {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.totalCost")
                        && <Item label={this.t("purchaseReq:items:totalCost")} value={currencyFormatter(pr.quantity * pr.amount)} col={3} />}
                      </div>
                    </div>)
                  }
                </div>
                : null
            }
              <div className="row">
                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.formDocs")
                && <Item label={this.t("purchaseReq:docs")} col={6}>
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
