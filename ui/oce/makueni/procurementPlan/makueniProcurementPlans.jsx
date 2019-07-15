import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, ppCountRemote, ppData, ppFilters } from './state';
import FiltersWrapper from '../filters/FiltersWrapper';

import '../makueni.less';

const NAME = 'MakueniPP';

class MakueniProcurementPlans extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
    
    console.log('------------------------------------------------');
  }
  
  componentDidMount() {
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
  
  render() {
    const { data, count } = this.state;
    const { navigate } = this.props;
    
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
              // dataFormat: mkContractLink(navigate),
            }, {
              title: 'Department',
              dataField: 'department',
            }, {
              title: 'Fiscal Year',
              dataField: 'fiscalYear',
            }]}
          />
        </div>
      </div>
    
    </div>);
  }
}

export default MakueniProcurementPlans;
