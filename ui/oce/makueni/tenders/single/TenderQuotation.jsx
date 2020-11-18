import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

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
        {isFeatureVisible("tenderQuotationEvaluationForm.closingDate")
        && <Item label="Closing Date" value={formatDate(tenderQuotationEvaluation.closingDate)}
                 col={6} className="padding-top-10" />}
      </div>

      {
        tenderQuotationEvaluation.bids !== undefined && isFeatureVisible("tenderQuotationEvaluationForm.bids")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Bids
                ({tenderQuotationEvaluation.bids.length})
              </div>
            </div>

            {
              tenderQuotationEvaluation.bids.map(bids => <div key={bids._id} className="box">
                {isFeatureVisible("tenderQuotationEvaluationForm.bids.supplier")
                && <div className="row">
                  <Item label="Supplier Name" value={bids.supplier.label} col={6} />
                  <Item label="Supplier ID" value={bids.supplier.code} col={6} />
                </div>}

                <div className="row">
                  {isFeatureVisible("tenderQuotationEvaluationForm.bids.supplierScore")
                  && <Item label="Supplier Score" value={bids.supplierScore} col={3} />}
                  {isFeatureVisible("tenderQuotationEvaluationForm.bids.supplierRanking")
                  && <Item label="Supplier Ranking" value={bids.supplierRanking} col={3} />}
                  {isFeatureVisible("tenderQuotationEvaluationForm.bids.quotedAmount")
                  && <Item label="Quoted Price" value={currencyFormatter(bids.quotedAmount)} col={3} />}
                  {isFeatureVisible("tenderQuotationEvaluationForm.bids.supplierResponsiveness")
                  && <Item label="Supplier Responsiveness" value={bids.supplierResponsiveness} col={3} />}
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row padding-top-10">
        {isFeatureVisible("tenderQuotationEvaluationForm.bids.supplierResponsiveness")
        && <Item label="Tender Quotation and Evaluation Documents" col={12}>
          {
            tenderQuotationEvaluation.formDocs.map(doc => <div key={doc._id}>
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
        </Item>
        }
      </div>
    </div>);
  }
}

export default fmConnect(TenderQuotation);
