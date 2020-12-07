import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
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
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">{this.t("purchaseReq:purchaseRequestNumber")}</div>
          <div className="item-value">{data.purchaseRequestNumber}</div>
        </div>
      </div>

      {
        data.purchRequisitions !== undefined
          ?
          data.purchRequisitions.map(preq => <div key={preq._id} className="box">
            <div className="row padding-top-10">
              <div className="col-md-3">
                <div className="item-label">{this.t("purchaseReq:requestedBy")}</div>
                <div className="item-value">{preq.requestedBy.label}</div>
              </div>
              <div className="col-md-3">
                <div className="item-label">{this.t("purchaseReq:chargeAccount")}</div>
                <div className="item-value">{preq.chargeAccount.label}</div>
              </div>
              <div className="col-md-3">
                <div className="item-label">{this.t("purchaseReq:requestApprovalDate")}</div>
                <div
                  className="item-value">{formatDate(preq.requestApprovalDate)}</div>
              </div>
              <div className="col-md-3">
                <div className="item-label">{this.t("purchaseReq:approvedDate")}</div>
                <div
                  className="item-value">{formatDate(preq.approvedDate)}</div>
              </div>
            </div>
            {
              preq.purchaseItems !== undefined
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">{this.t("purchaseReq:items")}
                      ({preq.purchaseItems.length})
                    </div>
                  </div>
                  {
                    preq.purchaseItems.map(pr => <div key={pr._id} className="box">
                      <div className="row">
                        <div className="col-md-6">
                          <div className="item-label">{this.t("purchaseReq:items:item")}</div>
                          <div className="item-value">{pr.planItem.item.label}</div>
                        </div>
                        <div className="col-md-6">
                          <div className="item-label">{this.t("purchaseReq:items:description")}</div>
                          <div className="item-value">{pr.description}</div>
                        </div>
                      </div>
                      <div className="row">
                        <div className="col-md-3">
                          <div className="item-label">{this.t("purchaseReq:items:unitOfIssue")}</div>
                          <div className="item-value">{pr.planItem.unitOfIssue.label}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">{this.t("purchaseReq:items:quantity")}</div>
                          <div className="item-value">{currencyFormatter(pr.quantity)}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">{this.t("purchaseReq:items:amount")}</div>
                          <div className="item-value">{currencyFormatter(pr.amount)}</div>
                        </div>
                        <div className="col-md-3">
                          <div className="item-label">{this.t("purchaseReq:items:totalCost")}</div>
                          <div
                            className="item-value">{currencyFormatter(pr.quantity * pr.amount)}</div>
                        </div>
                      </div>
                    </div>)
                  }
                </div>
                : null
            }
              <div className="row padding-top-10">
                <div className="col-md-6">
                  <div className="item-label">{this.t("purchaseReq:docs")}</div>
                  {
                    preq.formDocs.map(doc => <div key={doc._id}>
                      <OverlayTrigger
                        placement="bottom"
                        overlay={
                          <Tooltip id="download-tooltip">
                            {this.t("general:downloadFile:tooltip")}
                          </Tooltip>
                        }>

                        <a className="item-value download" href={doc.url} target="_blank">
                          <i className="glyphicon glyphicon-download"/>
                          <span>{doc.name}</span>
                        </a>
                      </OverlayTrigger>
                    </div>)
                  }
                </div>
                <div className="col-md-6">
                  <div className="item-label">{this.t("purchaseReq:approvedDate")}</div>
                  <div
                    className="item-value">{formatDate(preq.approvedDate)}</div>
                </div>
              </div>
          </div>
          )
          : null
      }

    </div>);
  }
}

export default PurchaseReq;
