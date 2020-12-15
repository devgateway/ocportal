import NoDataMessage from './NoData';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import React from "react";
import {tCreator} from "../../../translatable";
import defaultSingleTenderTabTypes from "./singleUtil";

const TenderQuotation = (props) => {

  const {data, isFeatureVisible} = props;
  const t = tCreator(props.translations);
  const {currencyFormatter, formatDate} = props.styling.tables;

  const getFeedbackSubject = () => {
    const {tenderTitle, department, fiscalYear} = props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
          + " - " + department.label
          + " - " + fiscalYear.name;
    }
    return escape(t("tenderQuotation:subject") + metadata);
  }

  const getQuotationView = (tenderQuotationEvaluation) => {
    return (<div>
      <div className="row">
        {isFeatureVisible("publicView.tenderQuotationEvaluation.closingDate")
        && <Item label={t("tenderQuotation:closingDate")} value={formatDate(tenderQuotationEvaluation.closingDate)}
                 col={6}/>}
      </div>

      {
        tenderQuotationEvaluation.bids !== undefined && isFeatureVisible("publicView.tenderQuotationEvaluation.bids")
            ? <div>
              <div className="row padding-top-10">
                <div className="col-md-12 sub-title">{t("tenderQuotation:bids")}
                  ({tenderQuotationEvaluation.bids.length})
                </div>
              </div>

              {
                tenderQuotationEvaluation.bids.map(bids => <div key={bids._id} className="box">
                  <div className="row">
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplier.label")
                    && <Item label={t("tenderQuotation:bids:supplierLabel")} value={bids.supplier.label} col={6}/>}
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplier.code")
                    && <Item label={t("tenderQuotation:bids:supplierCode")} value={bids.supplier.code} col={6}/>}
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierScore")
                    && <Item label={t("tenderQuotation:bids:supplierScore")} value={bids.supplierScore} col={3}/>}
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierRanking")
                    && <Item label={t("tenderQuotation:bids:supplierRanking")} value={bids.supplierRanking} col={3}/>}
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.quotedAmount")
                    && <Item label={t("tenderQuotation:bids:quotedAmount")} value={currencyFormatter(bids.quotedAmount)}
                             col={3}/>}
                    {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierResponsiveness")
                    && <Item label={t("tenderQuotation:bids:supplierResponsiveness")} value={bids.supplierResponsiveness}
                             col={3}/>}
                  </div>
                </div>)
              }
            </div>
            : null
      }

      <div className="row">
        {isFeatureVisible("publicView.tenderQuotationEvaluation.formDocs")
        && <Item label={t("tenderQuotation:docs")} col={12}>
          <FileDownloadLinks files={tenderQuotationEvaluation.formDocs} useDash/>
        </Item>
        }
      </div>
    </div>);
  }

  return (data === undefined ? <NoDataMessage translations={props.translations}/> : getQuotationView(data[0]));
}

TenderQuotation.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(TenderQuotation);
