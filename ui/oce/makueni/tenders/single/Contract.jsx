import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class Contract extends React.Component {
  
  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;
    
    if (data === undefined) {
      return null;
    }
  
    const contract = data[0];
  
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Reference Number</div>
          <div className="item-value">{contract.referenceNumber}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Contract Date</div>
          <div className="item-value">{formatDate(contract.contractDate)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Expiry Date</div>
          <div className="item-value">{formatDate(contract.expiryDate)}</div>
        </div>
      </div>
  
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Supplier Name</div>
          <div className="item-value">{contract.awardee.label}</div>
        </div>
        <div className="col-md-6">
          <div className="item-label">Supplier Postal Address</div>
          <div className="item-value">{contract.awardee.address}</div>
        </div>
      </div>
  
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Procuring Entity Name</div>
          <div className="item-value">{contract.procuringEntity.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Contract Value</div>
          <div className="item-value">{currencyFormatter(contract.contractValue)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Contract Approved Date</div>
          <div className="item-value">{formatDate(contract.contractApprovalDate)}</div>
        </div>
      </div>
  
      {
        contract.contractDocs !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">Contract Documents
                ({contract.contractDocs.length})
              </div>
            </div>
        
            {
              contract.contractDocs.map(contractDoc => <div key={contractDoc._id} className="box">
                <div className="row">
                  <div className="col-md-6">
                    <div className="item-label">Contract Document Type</div>
                    <div className="item-value">{contractDoc.contractDocumentType.label}</div>
                  </div>
                  <div className="col-md-6">
                    <div className="item-label">Contract Documents</div>
  
                    {
                      contractDoc.formDocs.map(doc => <div key={doc._id}>
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
              </div>)
            }
          </div>
          : null
      }
    </div>);
  }
}

export default Contract;
