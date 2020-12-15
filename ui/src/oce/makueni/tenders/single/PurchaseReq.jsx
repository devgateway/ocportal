import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import {tCreator} from "../../../translatable";
import defaultSingleTenderTabTypes from "./singleUtil";

const PurchaseReq = (props) => {
  const {data, isFeatureVisible} = props;
  const t = tCreator(props.translations);
  const {currencyFormatter, formatDate} = props.styling.tables;

  const getFeedbackSubject = () => {
    const {data, department, fiscalYear} = props;

    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.title
          + " - " + department.label
          + " - " + fiscalYear.name;
    }
    return escape(t("purchaseReq:label") + metadata);
  }


  const getPurchaseReq = () => {
    return (<div>
      <div className="row">
        {isFeatureVisible("publicView.tenderProcess.purchaseRequestNumber")
        && <Item label={t("purchaseReq:purchaseRequestNumber")} value={data.purchaseRequestNumber} col={3}/>}
      </div>

      {
        data.purchRequisitions !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions")
            ?
            data.purchRequisitions.map(preq => <div key={preq._id} className="box">
                  <div className="row">
                    {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestedBy")
                    && <Item label={t("purchaseReq:requestedBy")} value={preq.requestedBy.label} col={3}/>}

                    {isFeatureVisible("publicView.tenderProcess.purchRequisitions.chargeAccount")
                    && <Item label={t("purchaseReq:chargeAccount")} value={preq.chargeAccount.label} col={3}/>}

                    {isFeatureVisible("publicView.tenderProcess.purchRequisitions.requestApprovalDate")
                    && <Item label={t("purchaseReq:requestApprovalDate")}
                             value={formatDate(preq.requestApprovalDate)} col={3}/>}

                    {isFeatureVisible("publicView.tenderProcess.purchRequisitions.approvedDate")
                    && <Item label={t("purchaseReq:approvedDate")} value={formatDate(preq.approvedDate)} col={3}/>}
                  </div>
                  {
                    preq.purchaseItems !== undefined && isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems")
                        ? <div>
                          <div className="row padding-top-10">
                            <div className="col-md-12 sub-title">{t("purchaseReq:items")}
                              ({preq.purchaseItems.length})
                            </div>
                          </div>
                          {
                            preq.purchaseItems.map(pr => <div key={pr._id} className="box">
                              <div className="row display-flex">
                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.label")
                                && <Item label={t("purchaseReq:items:item")} value={pr.planItem.item.label} col={6}/>}

                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.description")
                                && <Item label={t("purchaseReq:items:description")} value={pr.description} col={6}/>}

                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.planItem.unitOfIssue")
                                && <Item label={t("purchaseReq:items:unitOfIssue")} value={pr.planItem.unitOfIssue.label}
                                         col={3}/>}

                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.quantity")
                                &&
                                <Item label={t("purchaseReq:items:quantity")} value={currencyFormatter(pr.quantity)} col={3}/>}

                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.amount")
                                && <Item label={t("purchaseReq:items:amount")} value={currencyFormatter(pr.amount)} col={3}/>}

                                {isFeatureVisible("publicView.tenderProcess.purchRequisitions.purchaseItems.totalCost")
                                && <Item label={t("purchaseReq:items:totalCost")}
                                         value={currencyFormatter(pr.quantity * pr.amount)} col={3}/>}
                              </div>
                            </div>)
                          }
                        </div>
                        : null
                  }
                  <div className="row">
                    {isFeatureVisible("publicView.tenderProcess.purchRequisitions.formDocs")
                    && <Item label={t("purchaseReq:docs")} col={6}>
                      <FileDownloadLinks files={preq.formDocs} useDash/>
                    </Item>}
                  </div>
                </div>
            )
            : null
      }

    </div>);
  }

  return (data === undefined ? <NoDataMessage translations={this.props.translations}/> : getPurchaseReq());
}

PurchaseReq.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(PurchaseReq);
