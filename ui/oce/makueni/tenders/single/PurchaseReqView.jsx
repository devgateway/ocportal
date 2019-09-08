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
        this.setState({
          data: data.purchaseRequisitions,
          department: data.department,
          fiscalYear: data.fiscalYear
        });
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
    const { selected, data, department, fiscalYear } = this.state;
    const tenderTitle = data.tender[0].tenderTitle;

    switch (selected) {
      case 1:
        return <Tender data={data.tender} department={department} fiscalYear={fiscalYear}
                       styling={this.props.styling}/>;

      case 2:
        return <PurchaseReq data={data} department={department} fiscalYear={fiscalYear}
                            styling={this.props.styling}/>;

      case 3:
        return <TenderQuotation data={data.tenderQuotationEvaluation} tenderTitle={tenderTitle}
                                department={department} fiscalYear={fiscalYear}
                                styling={this.props.styling}/>;

      case 4:
        return <ProfessionalOpinion data={data.professionalOpinion} tenderTitle={tenderTitle}
                                    department={department} fiscalYear={fiscalYear}
                                    styling={this.props.styling}/>;

      case 5:
        return <Notification data={data.awardNotification} tenderTitle={tenderTitle}
                             department={department} fiscalYear={fiscalYear}
                             styling={this.props.styling}/>;

      case 6:
        return <Award data={data.awardAcceptance} department={department} tenderTitle={tenderTitle}
                      fiscalYear={fiscalYear} styling={this.props.styling}/>;

      case 7:
        return <Contract data={data.contract} department={department} tenderTitle={tenderTitle}
                         fiscalYear={fiscalYear} styling={this.props.styling}/>;

      default:
        return <Tender data={data.tender} department={department} tenderTitle={tenderTitle}
                       fiscalYear={fiscalYear} styling={this.props.styling}/>;
    }
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

          <div className="col-md-offset-5 col-md-4">
            <button className="btn btn-info btn-subscribe pull-right" type="submit"
                    onClick={() => this.props.onSwitch('alerts', data._id, data.tender[0].tenderTitle)}>
              Receive Alerts For This Tender
            </button>
          </div>
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
