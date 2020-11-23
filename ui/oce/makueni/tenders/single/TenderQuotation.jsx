import NoDataMessage from './NoData';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import React from "react";

class TenderQuotation extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Tender Evaluation" + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const tenderQuotationEvaluation = data[0];

    return (<div>
      <div className="row">
        {isFeatureVisible("publicView.tenderQuotationEvaluation.closingDate")
        && <Item label="Closing Date" value={formatDate(tenderQuotationEvaluation.closingDate)}
                 col={6} />}
      </div>

      {
        tenderQuotationEvaluation.bids !== undefined && isFeatureVisible("publicView.tenderQuotationEvaluation.bids")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Bids
                ({tenderQuotationEvaluation.bids.length})
              </div>
            </div>

            {
              tenderQuotationEvaluation.bids.map(bids => <div key={bids._id} className="box">
                <div className="row">
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplier.label")
                  && <Item label="Supplier Name" value={bids.supplier.label} col={6} />}
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplier.code")
                  && <Item label="Supplier ID" value={bids.supplier.code} col={6} />}
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierScore")
                  && <Item label="Supplier Score" value={bids.supplierScore} col={3} />}
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierRanking")
                  && <Item label="Supplier Ranking" value={bids.supplierRanking} col={3} />}
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.quotedAmount")
                  && <Item label="Quoted Price" value={currencyFormatter(bids.quotedAmount)} col={3} />}
                  {isFeatureVisible("publicView.tenderQuotationEvaluation.bids.supplierResponsiveness")
                  && <Item label="Supplier Responsiveness" value={bids.supplierResponsiveness} col={3} />}
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row">
        {isFeatureVisible("publicView.tenderQuotationEvaluation.formDocs")
        && <Item label="Tender Quotation and Evaluation Documents" col={12}>
          <FileDownloadLinks files={tenderQuotationEvaluation.formDocs} useDash />
        </Item>
        }
      </div>
    </div>);
  }
}

export default fmConnect(TenderQuotation);
