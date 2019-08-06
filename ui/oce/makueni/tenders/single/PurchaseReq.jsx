import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class PurchaseReq extends React.Component {
  
  render() {
    const { data } = this.props;
    
    if (data === undefined) {
      return null;
    }
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Purchase Requisition Title</div>
          <div className="item-value">{data.title}</div>
        </div>
        <div className="col-md-6">
          <div className="item-label">Purchase Request Number</div>
          <div className="item-value">{data.purchaseRequestNumber}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Requested By</div>
          <div className="item-value">{data.requestedBy.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Charge Account</div>
          <div className="item-value">{data.chargeAccount.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Request Approval Date</div>
          <div
            className="item-value">{new Date(data.requestApprovalDate).toLocaleDateString()}</div>
        </div>
      </div>
      
      {
        data.purchaseItems !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Purchase Requisition Items
                ({data.purchaseItems.length})
              </div>
            </div>
            
            {
              data.purchaseItems.map(pr => <div key={pr._id} className="box">
                <div className="row">
                  <div className="col-md-12">
                    <div className="item-label">Item</div>
                    <div className="item-value">{pr.planItem.description} - {pr.planItem.item.label}</div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-3">
                    <div className="item-label">Unit of Issue</div>
                    <div className="item-value">{pr.unit}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Quantity</div>
                    <div className="item-value">{pr.quantity}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Unit Price</div>
                    <div className="item-value">{pr.amount}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">Total Cost</div>
                    <div className="item-value">{pr.quantity * pr.amount}</div>
                  </div>
                </div>
              </div>)
            }
          </div>
          : null
      }
      
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Procurement Plan Documents</div>
          
          {
            data.formDocs.map(doc => <div key={doc._id}>
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
          <div className="item-label">Approved Date</div>
          <div
            className="item-value">{new Date(data.approvedDate).toLocaleDateString()}</div>
        </div>
      </div>
    </div>);
  }
}

export default PurchaseReq;
