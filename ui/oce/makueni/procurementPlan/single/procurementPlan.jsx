import CRDPage from '../../../corruption-risk/page';
import { ppState } from '../state';
import { API_ROOT } from '../../../state/oce-state';

class ProcurementPlan extends CRDPage {
  constructor(props) {
    super(props);
    
    this.state = {};
    
    this.ppID = ppState.input({
      name: 'ppID',
    });
    
    this.ppInfoUrl = ppState.mapping({
      name: 'ppInfoUrl',
      deps: [this.ppID],
      mapper: id => `${API_ROOT}/makueni/procurementPlan/id/${id}`,
    });
    
    this.ppInfo = ppState.remote({
      name: 'ppInfo',
      url: this.ppInfoUrl,
    });
  }
  
  componentDidMount() {
    super.componentDidMount();
    
    const { id } = this.props;
    
    this.ppID.assign('PP', id);
    
    this.ppInfo.addListener('PP', () => {
      this.ppInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }
  
  componentWillUnmount() {
    this.ppInfo.removeListener('PP');
  }
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
    
    return (<div className="procurement-plan">
      <div className="row">
        <a href="#!/procurement-plan" onClick={() => navigate()} className="back-link col-md-12">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span>
          Go Back
        </span>
        </a>
      </div>
      
      {
        data !== undefined
          ? <div className="row padding-top">
            <div className="col-md-8">
              <div className="item-label"> Department</div>
              <div className="item-value">{data.department.label}</div>
            </div>
            <div className="col-md-4">
              <div className="item-label"> Fiscal Year</div>
              <div className="item-value">{data.fiscalYear.name}</div>
            </div>
          </div>
          : null
      }
    </div>);
  }
}

export default ProcurementPlan;
