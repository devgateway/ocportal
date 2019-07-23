import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, tendersCountRemote, tendersData } from './state';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

import '../makueni.less';


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
  
  tenderLink(navigate) {
    return (tender) => (<div>
      {
        tender !== undefined
          ? <a href={`#!/tender/t/${tender._id}`} onClick={() => navigate('t', tender._id)}
               className="more-details-link">
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
          ? <a href={`#!/tender/p/${project._id}`} onClick={() => navigate('t', project._id)}
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
          tender && tender.formDocs.map(doc => <div key={doc}>
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
    // console.log(JSON.stringify(this.state, null, '\t'));
    
    // mtFilters.assign('[[AAAA]]', new Map({ age: 5 }));
    
    const { data, count } = this.state;
    const { navigate } = this.props;
    
    return (<div className="container-fluid dashboard-default">
      
      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              selected="tender"/>
      
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
      </div>
    
    </div>);
  }
}

export default MakueniTenders;
