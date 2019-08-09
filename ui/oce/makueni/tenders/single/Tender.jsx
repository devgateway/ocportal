import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import FeedbackPage from '../../FeedbackPage';

class Tender extends FeedbackPage {
  getFeedbackSubject() {
    return escape("Makueni Public Portal - Tender");
  }
  
  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;
    
    if (data === undefined) {
      return null;
    }
    
    const tender = data[0];
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Tender Name</div>
          <div className="item-value">{tender.tenderTitle}</div>
        </div>
        <div className="col-md-6">
          <div className="item-label">Tender ID</div>
          <div className="item-value">{tender.tenderNumber}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Invitation to Tender Date</div>
          <div className="item-value">{formatDate(tender.invitationDate)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Closing Date</div>
          <div className="item-value">{formatDate(tender.closingDate)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Procurement Method</div>
          <div className="item-value">{tender.procurementMethod.label}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-12">
          <div className="item-label">Tender Objective</div>
          <div className="item-value">{tender.objective}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Tender Issued By</div>
          <div className="item-value">{tender.issuedBy.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Procuring Entity Address</div>
          <div className="item-value">{tender.issuedBy.address}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Procuring Entity Email</div>
          <div className="item-value">{tender.issuedBy.emailAddress}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Tender Value</div>
          <div className="item-value">{currencyFormatter(tender.tenderValue)}</div>
        </div>
        <div className="col-md-6">
          <div className="item-label">Target Group</div>
          {
            tender.targetGroup !== undefined
              ? <div className="item-value">{tender.targetGroup.label}</div>
              : null
          }
        </div>
      </div>
      
      {
        tender.tenderItems !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Tender Items
                ({tender.tenderItems.length})
              </div>
            </div>
            
            {
              tender.tenderItems.map(tenderItem => <div key={tenderItem._id} className="box">
                <div className="row">
                  <div className="col-md-12">
                    <div className="item-label">Item</div>
                    <div
                      className="item-value">{tenderItem.purchaseItem.planItem.description} - {tenderItem.purchaseItem.planItem.item.label}</div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-3">
                    <div className="item-label">Unit of Issue</div>
                    <div className="item-value">{tenderItem.unitOfIssue}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Quantity</div>
                    <div className="item-value">{currencyFormatter(tenderItem.quantity)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Unit Price</div>
                    <div className="item-value">{currencyFormatter(tenderItem.unitPrice)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Total Cost</div>
                    <div
                      className="item-value">{currencyFormatter(tenderItem.quantity * tenderItem.unitPrice)}</div>
                  </div>
                </div>
              </div>)
            }
          </div>
          : null
      }
      
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Upload Tender</div>
          {
            tender.formDocs.map(doc => <div key={doc._id}>
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
        <div className="col-md-6">
          <div className="item-label">Tender Link</div>
          <div className="item-value">{tender.tenderLink}</div>
        </div>
      </div>
      
      {this.getFeedbackMessage()}
    </div>);
  }
}

export default Tender;
