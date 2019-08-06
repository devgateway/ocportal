import CRDPage from '../../../corruption-risk/page';
import { mtState } from '../state';
import cn from 'classnames';
import { API_ROOT } from '../../../state/oce-state';
import Tender from './Tender';
import PurchaseReq from './PurchaseReq';
import TenderQuotation from './TenderQuotation';
import ProfessionalOpinion from './ProfessionalOpinion';
import Notification from './Notification';
import Award from './Award';
import Contract from './Contract';

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
      name: 'Tender',
      tab: 1
    }, {
      name: 'Purchase Req',
      tab: 2
    }, {
      name: 'Tender Quotation',
      tab: 3
    }, {
      name: 'Professional Opinion',
      tab: 4
    }, {
      name: 'Notification',
      tab: 5
    }, {
      name: 'Award',
      tab: 6
    }, {
      name: 'Contract',
      tab: 7
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
        this.setState({ data: data.purchaseRequisitions });
      });
    });
  }
  
  componentWillUnmount() {
    this.prInfo.removeListener('PR');
  }
  
  isActive(option) {
    const { selected } = this.state;
    if (selected === undefined || selected === '') {
      return false;
    }
    return selected === option;
  }
  
  changeTab(option) {
    this.setState({ selected: option });
  }
  
  displayTab() {
    const { selected, data } = this.state;
  
    switch (selected) {
      case 1:
        return <Tender data={data.tender} styling={this.props.styling}/>;
      
      case 2:
        return <PurchaseReq data={data} styling={this.props.styling}/>;
      
      case 3:
        return <TenderQuotation data={data.tenderQuotationEvaluation} styling={this.props.styling}/>;
      
      case 4:
        return <ProfessionalOpinion data={data.professionalOpinion} styling={this.props.styling}/>;
      
      case 5:
        return <Notification data={data.awardNotification} styling={this.props.styling}/>;
      
      case 6:
        return <Award data={data.awardAcceptance} styling={this.props.styling}/>;
      
      case 7:
        return <Contract data={data.contract} styling={this.props.styling}/>;
      
      default:
        return <Tender data={data.tender} styling={this.props.styling}/>;
    }
  }
  
  render() {
    const { navigate } = this.props;
    const { data } = this.state;
    
    // console.log(data);
    
    return (<div className="tender makueni-form">
      <div className="row">
        <div className="col-md-12 navigation-view">
          {
            this.tabs.map(tab => {
              return (<a
                  key={tab.tab}
                  href="javascript:void(0);"
                  className={cn('', { active: this.isActive(tab.tab) })}
                  onClick={() => this.changeTab(tab.tab)}
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
      
      {
        data !== undefined
          ? this.displayTab()
          : null
      }
    </div>);
  }
}

export default PurchaseReqView;
