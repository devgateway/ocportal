import { API_ROOT } from '../../../state/oce-state';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { mtState } from '../state';
import FeedbackPage from '../../FeedbackPage';

class Project extends FeedbackPage {
  constructor(props) {
    super(props);
    
    this.state = {};
    
    this.projectID = mtState.input({
      name: 'projectID',
    });
    
    this.projectInfoUrl = mtState.mapping({
      name: 'projectInfoUrl',
      deps: [this.projectID],
      mapper: id => `${API_ROOT}/makueni/project/id/${id}`,
    });
    
    this.projectInfo = mtState.remote({
      name: 'projectInfo',
      url: this.projectInfoUrl,
    });
  }
  
  componentDidMount() {
    const { id } = this.props;
    
    this.projectID.assign('Project', id);
    
    this.projectInfo.addListener('Project', () => {
      this.projectInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }
  
  componentWillUnmount() {
    this.projectInfo.removeListener('Project');
  }
  
  getFeedbackSubject() {
    const { data } = this.state;
    
    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.projects.projectTitle
        + " - " + data.department.label
        + " - " + data.fiscalYear.name;
    }
    return escape("Project" + metadata);
  }
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
  
    const { currencyFormatter, formatDate } = this.props.styling.tables;
    
    return (<div className="project makueni-form">
      <div className="row">
        <a href="#!/tender" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span className="back-text">
          Go Back
        </span>
        </a>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">Project</h1>
        </div>
      </div>
      
      {
        data !== undefined
          ? <div>
            <div className="row">
              <div className="col-md-6">
                <div className="item-label">Project Title</div>
                <div className="item-value">{data.projects.projectTitle}</div>
              </div>
              <div className="col-md-6">
                <div className="item-label">Cabinet Paper</div>
                {
                  data.projects.cabinetPaper.formDocs.map(doc => <div key={doc._id}>
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
    
            <div className="row padding-top-10">
              <div className="col-md-6">
                <div className="item-label">Amount Budgeted</div>
                <div className="item-value">{currencyFormatter(data.projects.amountBudgeted)}</div>
              </div>
              <div className="col-md-6">
                <div className="item-label">Amount Requested</div>
                <div className="item-value">{currencyFormatter(data.projects.amountRequested)}</div>
              </div>
            </div>
    
            <div className="row padding-top-10">
              <div className="col-md-6">
                <div className="item-label">Sub-Counties</div>
                <div className="item-value">{data.projects.subcounties.map(item => item.label).join(', ')}</div>
              </div>
              <div className="col-md-6">
                <div className="item-label">Wards</div>
                <div className="item-value">{data.projects.wards.map(item => item.label).join(', ')}</div>
              </div>
            </div>
    
            <div className="row padding-top-10">
              <div className="col-md-6">
                <div className="item-label">Approved Date</div>
                <div className="item-value">{formatDate(data.projects.approvedDate)}</div>
              </div>
            </div>
          </div>
          : null
      }
  
      {this.getFeedbackMessage()}
    </div>);
  }
  
}

export default Project;
