import { OverlayTrigger, Tooltip } from 'react-bootstrap';

class ProfessionalOpinion extends React.Component {
  
  render() {
    const { data } = this.props;
    
    if (data === undefined) {
      return null;
    }
    
    const professionalOpinion = data[0];
    
    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">Professional Opinion Date</div>
          <div
            className="item-value">{new Date(professionalOpinion.professionalOpinionDate).toLocaleDateString()}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Awardee</div>
          <div className="item-value">{professionalOpinion.awardee.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">Recommended Award Amount</div>
          <div className="item-value">{professionalOpinion.recommendedAwardAmount}</div>
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-6">
          <div className="item-label">Professional Opinion Documents</div>
          
          {
            professionalOpinion.formDocs.map(doc => <div key={doc._id}>
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
            className="item-value">{new Date(professionalOpinion.approvedDate).toLocaleDateString()}</div>
        </div>
      </div>
    
    </div>);
  }
}

export default ProfessionalOpinion;
