import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, ppCountRemote, ppData, ppFilters } from './state';
import FiltersWrapper from '../filters/FiltersWrapper';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

import '../makueni.less';
import ProcurementPlan from './single/procurementPlan';

const NAME = 'MakueniPP';

class MakueniProcurementPlans extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
  }
  
  componentDidMount() {
    super.componentDidMount();
    
    ppData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    ppCountRemote.addListener(NAME, () => this.updateBindings());
  }
  
  componentWillUnmount() {
    ppData.removeListener(NAME);
    page.removeListener(NAME);
    pageSize.removeListener(NAME);
    ppCountRemote.removeListener(NAME);
  }
  
  updateBindings() {
    Promise.all([
      ppData.getState(NAME),
      page.getState(NAME),
      pageSize.getState(NAME),
      ppCountRemote.getState(NAME),
    ])
    .then(([data, page, pageSize, ppCount]) => {
      this.setState({
        data,
        page,
        pageSize,
        count: ppCount,
      });
    });
  }
  
  shouldComponentUpdate(nextProps, nextState) {
    return JSON.stringify(this.state) !== JSON.stringify(nextState)
      || JSON.stringify(this.props) !== JSON.stringify(nextProps);
  }
  
  filters() {
    return <FiltersWrapper filters={ppFilters} translations={this.props.translations}/>;
  }
  
  ppLink(navigate) {
    return (ppId) => (
      <a href={`#!/procurement-plan/pp/${ppId}`} onClick={() => navigate('pp', ppId)}
         className="more-details-link">
        More Details
      </a>
    );
  }
  
  downloadFiles() {
    return (formDocs) => (<div>
        {
          formDocs.map(doc => <div key={doc.id}>
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
              selected="procurement-plan"/>
      
      <div className="makueni-procurement-plan content row">
        <div className="col-md-3 filters">
          <div className="row">
            <div className="filters-hint col-md-12">
              {this.t('filters:hint')}
            </div>
            {this.filters()}
          </div>
        </div>
        
        <div className="col-md-9">
          {
            navigationPage === undefined
              ? <div>
                <h1>Makueni Procurement Plans</h1>
                
                <BootstrapTableWrapper
                  bordered
                  data={data}
                  page={this.state.page}
                  pageSize={this.state.pageSize}
                  onPageChange={newPage => page.assign(NAME, newPage)}
                  onSizePerPageList={newPageSize => pageSize.assign(NAME, newPageSize)}
                  count={count}
                  columns={[{
                    title: 'ID',
                    dataField: 'id',
                    width: '20%',
                    dataFormat: this.ppLink(navigate),
                  }, {
                    title: 'Department',
                    dataField: 'department',
                  }, {
                    title: 'Fiscal Year',
                    dataField: 'fiscalYear',
                  }, {
                    title: 'Procurement Plan Files',
                    dataField: 'formDocs',
                    dataFormat: this.downloadFiles(),
                  }]}
                />
              </div>
              :
              <ProcurementPlan id={id} navigate={navigate} translations={this.props.translations}/>
          }
        </div>
      </div>
    
    </div>);
  }
}

export default MakueniProcurementPlans;
