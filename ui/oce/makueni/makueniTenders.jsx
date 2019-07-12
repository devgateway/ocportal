import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import BootstrapTableWrapper from '../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, tendersCountRemote, tendersData } from './state';
import { mkContractLink } from '../corruption-risk/tools';

import './makueniTenders.less';


const NAME = 'MakueniTenders';

class MakueniTenders extends CRDPage {
  constructor(props) {
    super(props);
    
    this.state = {
      data: []
    };
  
    console.log("------------------------------------------------");
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
  
  render() {
    console.log(JSON.stringify(this.state, null, '\t'));
    
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
              title: 'Tender name',
              dataField: 'name',
              width: '20%',
              dataFormat: mkContractLink(navigate),
            }, {
              title: 'OCID',
              dataField: 'id',
              dataFormat: mkContractLink(navigate),
            }, {
              title: 'Award status',
              dataField: 'awardStatus',
            }, {
              title: 'Tender amount',
              dataField: 'tenderAmount',
            }, {
              title: this.t('crd:contracts:list:awardAmount'),
              dataField: 'awardAmount',
            }, {
              title: 'Number of bidders',
              dataField: 'nrBidders',
            }, {
              title: 'Number of flags',
              dataField: 'nrFlags',
            }]}
          />
          
          {/*<TendersList*/}
          {/*  {...wireProps(this)}*/}
          {/*  requestNewData={(_, data) => this.setState({ table: data })}*/}
          {/*  dataEP="procuringEntitiesByFlags"*/}
          {/*  countEP="procuringEntitiesByFlags/count"*/}
          {/*  filters={filters}*/}
          {/*  // searchQuery={searchQuery}*/}
          {/*  // navigate={navigate}*/}
          {/*/>*/}
          
          {/*<Archive*/}
          {/*  {...wireProps(this)}*/}
          {/*  requestNewData={this.requestNewData.bind(this)}*/}
          {/*  searchQuery={searchQuery}*/}
          {/*  doSearch={doSearch}*/}
          {/*  navigate={navigate}*/}
          {/*  className="procuring-entities-page"*/}
          {/*  topSearchPlaceholder={this.t('crd:procuringEntities:top-search')}*/}
          {/*  List={TendersList}*/}
          {/*  dataEP="procuringEntitiesByFlags"*/}
          {/*  countEP="procuringEntitiesByFlags/count"*/}
          {/*/>*/}
        </div>
      </div>
    
    </div>);
  }
}

export default MakueniTenders;
