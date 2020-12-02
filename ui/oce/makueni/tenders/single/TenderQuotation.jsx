import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import translatable from "../../../translatable";

class TenderQuotation extends translatable(React.Component) {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape(this.t("tenderQuotation:subject") + metadata);
  }

  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const tenderQuotationEvaluation = data[0];

    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">{this.t("tenderQuotation:closingDate")}</div>
          <div
            className="item-value">{formatDate(tenderQuotationEvaluation.closingDate)}</div>
        </div>
      </div>

      {
        tenderQuotationEvaluation.bids !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("tenderQuotation:bids")}
                ({tenderQuotationEvaluation.bids.length})
              </div>
            </div>

            {
              tenderQuotationEvaluation.bids.map(bids => <div key={bids._id} className="box">
                <div className="row">
                  <div className="col-md-6">
                    <div className="item-label">{this.t("tenderQuotation:bids:supplierLabel")}</div>
                    <div className="item-value">{bids.supplier.label}</div>
                  </div>
                  <div className="col-md-6">
                    <div className="item-label">{this.t("tenderQuotation:bids:supplierCode")}</div>
                    <div className="item-value">{bids.supplier.code}</div>
                  </div>
                </div>

                <div className="row">
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tenderQuotation:bids:supplierScore")}</div>
                    <div className="item-value">{bids.supplierScore}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tenderQuotation:bids:supplierRanking")}</div>
                    <div className="item-value">{bids.supplierRanking}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tenderQuotation:bids:quotedAmount")}</div>
                    <div className="item-value">{currencyFormatter(bids.quotedAmount)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tenderQuotation:bids:supplierResponsiveness")}</div>
                    <div className="item-value">{bids.supplierResponsiveness}</div>
                  </div>
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row padding-top-10">
        <div className="col-md-12">
          <div className="item-label">{this.t("tenderQuotation:docs")}</div>

          {
            tenderQuotationEvaluation.formDocs.map(doc => <div key={doc._id}>
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
      </div>
    </div>);
  }
}

export default TenderQuotation;
