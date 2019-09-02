import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { mtFilters, page, pageSize, tendersCountRemote, tendersData } from './state';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Map } from 'immutable';

import '../makueni.less';
import Project from './single/Project';
import PurchaseReqView from './single/PurchaseReqView';
import FiltersTendersWrapper from '../filters/FiltersTendersWrapper';


const NAME = 'MakueniTenders';

class MakueniTenders extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
  }
  
  componentDidMount() {
    tendersData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    tendersCountRemote.addListener(NAME, () => this.updateBindings());
  }
  
  componentWillUnmount() {
    // reset all filters when we unmount this component
    mtFilters.assign('NAME', Map());
    
    tendersData.removeListener(NAME);
    page.removeListener(NAME);
    pageSize.removeListener(NAME);
    tendersCountRemote.removeListener(NAME);
  }
  
  updateBindings() {
    Promise.all([
      tendersData.getState(NAME),
      page.getState(NAME),
      pageSize.getState(NAME),
      tendersCountRemote.getState(NAME),
    ])
    .then(([data, page, pageSize, tendersCount]) => {
      this.setState({
        data,
        page,
        pageSize,
        count: tendersCount,
      });
    });
  }
  
  shouldComponentUpdate(nextProps, nextState) {
    return JSON.stringify(this.state) !== JSON.stringify(nextState)
      || JSON.stringify(this.props) !== JSON.stringify(nextProps);
  }
  
  filters() {
    return <FiltersTendersWrapper filters={mtFilters} translations={this.props.translations}/>;
  }
  
  tenderLink(navigate) {
    return (tender) => (<div className="tender-title">
      {
        tender !== undefined
          ? <a href={`#!/tender/t/${tender.purchaseReqId}`}
               onClick={() => navigate('t', tender.purchaseReqId)} className="more-details-link">
            {tender.tenderTitle}
          </a>
          : 'No Tender'
      }
    </div>);
  }
  
  projectLink(navigate) {
    return (project) => (<div>
      {
        project !== undefined
          ? <a href={`#!/tender/p/${project._id}`} onClick={() => navigate('p', project._id)}
               className="more-details-link">
            {project.projectTitle}
          </a>
          : 'No Project'
      }
    </div>);
  }
  
  downloadFiles() {
    return (tender) => (<div>
        {
          tender && tender.formDocs && tender.formDocs.map(doc => <div key={doc}>
            <OverlayTrigger
              placement="bottom"
              overlay={
                <Tooltip id="download-tooltip">
                  Click to download the file
                </Tooltip>
              }>
              
              <a className="download-file" href={doc.url} target="_blank">
                <i className="glyphicon glyphicon-download"/>
                <span>{doc.name}</span>
              </a>
            </OverlayTrigger>
          </div>)
        }
      </div>
    );
  }
  
  render() {
    const { data, count } = this.state;
    const { navigate, route } = this.props;
    const [navigationPage, id] = route;
    
    return (<div className="container-fluid dashboard-default">
      
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="tender"/>
      
      <div className="makueni-tenders content row">
        <div className="col-md-3 col-sm-3 filters">
          <div className="row">
            <div className="filters-hint col-md-12">
              {this.t('filters:hint')}
            </div>
            {this.filters()}
          </div>
        </div>
        
        <div className="col-md-9 col-sm-9 col-main-content">
          {
            navigationPage === undefined
              ? <div>
                <h1>Makueni Tenders</h1>
                
                <BootstrapTableWrapper
                  bordered
                  data={data}
                  page={this.state.page}
                  pageSize={this.state.pageSize}
                  onPageChange={newPage => page.assign(NAME, newPage)}
                  onSizePerPageList={newPageSize => pageSize.assign(NAME, newPageSize)}
                  count={count}
                  columns={[{
                    title: 'Tender Title',
                    dataField: 'tender',
                    dataFormat: this.tenderLink(navigate),
                  }, {
                    title: 'Department',
                    dataField: 'department',
                  }, {
                    title: 'Fiscal Year',
                    dataField: 'fiscalYear',
                  }, {
                    title: 'Closing Date',
                    dataField: 'tender',
                    dataFormat: (cell, row) => cell !== undefined ? new Date(cell.closingDate).toLocaleDateString() : ''
                  }, {
                    title: 'Tender Value',
                    dataField: 'tender',
                    dataFormat: (cell, row) => cell !== undefined ? this.props.styling.charts.hoverFormatter(cell.tenderValue) : ''
                  }, {
                    title: 'Project',
                    dataField: 'project',
                    dataFormat: this.projectLink(navigate)
                  }, {
                    title: 'Tender Documents',
                    dataField: 'tender',
                    dataFormat: this.downloadFiles(),
                  }]}
                />
              </div>
              : navigationPage === 't'
              ? <PurchaseReqView selected={1} id={id} navigate={navigate}
                                 onSwitch={this.props.onSwitch}
                                 translations={this.props.translations}
                                 styling={this.props.styling}/>
              : <Project id={id} navigate={navigate} translations={this.props.translations}
                         styling={this.props.styling}/>
          }
        </div>
      </div>
      
      <div className="alerts-container">
        <div className="row alerts-button">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('alerts')}>Subscribe to Email Alerts
            </button>
          </div>
        </div>
      </div>
    </div>);
  }
}

export default MakueniTenders;
