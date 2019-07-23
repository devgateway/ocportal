import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class Award extends React.Component {
  
  render() {
    const { data } = this.props;
  
    if (data === undefined) {
      return null;
    }
  
    const awardAcceptance = data[0];
  
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-3">
          <div className="item-label">Accepted Award Value</div>
          <div className="item-value">{awardAcceptance.acceptedAwardValue}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Date</div>
          <div className="item-value">{new Date(awardAcceptance.acceptanceDate).toLocaleDateString()}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Supplier Name</div>
          <div className="item-value">{awardAcceptance.awardee.label}</div>
        </div>
        <div className="col-md-3">
          <div className="item-label">Supplier ID</div>
          <div className="item-value">{awardAcceptance.awardee.code}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-12">
          <div className="item-label">Letter of Acceptance of Award</div>
      
          {
            awardAcceptance.formDocs.map(doc => <div key={doc._id}>
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

export default Award;
