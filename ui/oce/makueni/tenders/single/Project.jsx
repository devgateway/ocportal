import CRDPage from '../../../corruption-risk/page';
import { API_ROOT } from '../../../state/oce-state';
import { mtState } from '../state';

class Project extends CRDPage {
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
    super.componentDidMount();
    
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
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
    
    return (<div className="project makueni-form">
      <div className="row">
        <a href="#!/tender" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span>
          Go Back
        </span>
        </a>
      </div>
  
      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">Project</h1>
        </div>
      </div>
    </div>);
  }

}

export default Project;
