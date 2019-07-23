import CRDPage from '../../../corruption-risk/page';
import { mtState } from '../state';
import cn from 'classnames';
import { API_ROOT } from '../../../state/oce-state';

class PurchaseReqView extends CRDPage {
  constructor(props) {
    super(props);
    
    this.state = {
      selected: props.selected || ''
    };
    
    this.prID = mtState.input({
      name: 'prID',
    });
    
    this.prInfoUrl = mtState.mapping({
      name: 'prInfoUrl',
      deps: [this.prID],
      mapper: id => `${API_ROOT}/makueni/purchaseReq/id/${id}`,
    });
    
    this.prInfo = mtState.remote({
      name: 'prInfo',
      url: this.prInfoUrl,
    });
    
    this.tabs = [{
      name: 'Tender'
    }, {
      name: 'Purchase Req'
    }, {
      name: 'Tender Quotation'
    }, {
      name: 'Professional Opinion'
    }, {
      name: 'Notification'
    }, {
      name: 'Award'
    }, {
      name: 'Contract'
    }];
  
    this.isActive = this.isActive.bind(this);
    this.changeTab = this.changeTab.bind(this);
  }
  
  
  componentDidMount() {
    super.componentDidMount();
    
    const { id } = this.props;
    
    this.prID.assign('PR', id);
    
    this.prInfo.addListener('PR', () => {
      this.prInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }
  
  componentWillUnmount() {
    this.prInfo.removeListener('PR');
  }
  
  isActive(option) {
    const { selected } = this.state;
    if (selected === '') {
      return false;
    }
    return selected === option;
  }
  
  changeTab(option) {
    this.setState({ selected: option });
  }
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
    
    return (<div className="tender makueni-form">
      <div className="row">
        <div className="col-md-12 navigation-view">
          {
            this.tabs.map(tab => {
              return (<a
                  key={tab.name}
                  href="javascript:void(0);"
                  className={cn('', { active: this.isActive(tab.name) })}
                  onClick={() => this.changeTab(tab.name)}
                >
                  {tab.name}
                </a>
              );
            })
          }
        </div>
      </div>
  
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
    </div>);
  }
}

export default PurchaseReqView;
