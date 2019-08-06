import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class TenderQuotation extends React.Component {
  
  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;
    
    if (data === undefined) {
      return null;
    }
    
    const tenderQuotationEvaluation = data[0];
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Closing Date</div>
          <div
            className="item-value">{formatDate(tenderQuotationEvaluation.closingDate)}</div>
        </div>
      </div>
  
      {
        tenderQuotationEvaluation.bids !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Purchase Requisition Items
                ({tenderQuotationEvaluation.bids.length})
              </div>
            </div>
        
            {
              tenderQuotationEvaluation.bids.map(bids => <div key={bids._id} className="box">
                <div className="row">
                  <div className="col-md-6">
                    <div className="item-label">Supplier Name</div>
                    <div className="item-value">{bids.supplier.label}</div>
                  </div>
                  <div className="col-md-6">
                    <div className="item-label">Supplier ID</div>
                    <div className="item-value">{bids.supplier.code}</div>
                  </div>
                </div>
  
                <div className="row">
                  <div className="col-md-3">
                    <div className="item-label">Supplier Score</div>
                    <div className="item-value">{currencyFormatter(bids.supplierScore)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Supplier Ranking</div>
                    <div className="item-value">{currencyFormatter(bids.supplierRanking)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Quoted Price</div>
                    <div className="item-value">{currencyFormatter(bids.quotedAmount)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Supplier Responsiveness</div>
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
          <div className="item-label">Tender Quotation and Evaluation Documents</div>
      
          {
            tenderQuotationEvaluation.formDocs.map(doc => <div key={doc._id}>
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
        </div>
      </div>
      
    </div>);
  }
}

export default TenderQuotation;
