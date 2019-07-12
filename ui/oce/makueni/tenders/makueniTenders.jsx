import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, tendersCountRemote, tendersData } from './state';

import './makueniTenders.less';


const NAME = 'MakueniTenders';

class MakueniTenders extends CRDPage {
  
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
    
    console.log('------------------------------------------------');
  }
  
  componentDidMount() {
    tendersData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    tendersCountRemote.addListener(NAME, () => this.updateBindings());
  }
  
  componentWillUnmount() {
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
  
  
  render() {
    console.log(JSON.stringify(this.state, null, '\t'));
    
    // mtFilters.assign('[[AAAA]]', new Map({ age: 5 }));
    
    const { data, count } = this.state;
    const { navigate } = this.props;
    
    return (<div className="container-fluid dashboard-default">
      
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              selected="makueni"/>
      
      <div className="makueni-tenders content row">
        <div className="col-md-3 filters">
          <h3>Filters</h3>
        </div>
        
        <div className="col-md-9">
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

export default MakueniTenders;
