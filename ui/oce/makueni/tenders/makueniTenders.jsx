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
import Footer from '../../layout/footer';
import React from 'react';
import fmConnect from "../../fm/fm";
import FileDownloadLinks from "./single/FileDownloadLinks";


const NAME = 'MakueniTenders';

class MakueniTenders extends CRDPage {

  constructor(props) {
    super(props);
    this.introjsCnt = 0;

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

  resetPage() {
    page.assign(NAME, 1);
  }

  filters() {
    return <FiltersTendersWrapper filters={mtFilters} resetPage={this.resetPage.bind(this)} translations={this.props.translations}/>;
  }

  showDataStep() {
    return this.introjsCnt++ < 6;
  }

  tenderLink(navigate) {
    return (tender) => (<div className="tender-title" data-step={this.showDataStep()?9:""}
    data-intro={this.showDataStep()?"Click on the Tender Title to view the tender details," +
    " purchase requisition, tender quotation, professional opinion, notification, award and " +
    "contract for each tender process.":""}>
      {
        tender !== undefined
          ? <a href={`#!/tender/t/${tender.purchaseReqId}`}
               onClick={() => navigate('t', tender.purchaseReqId)} className="more-details-link">
            {tender.tenderTitle && tender.tenderTitle.toUpperCase()}
          </a>
          : 'No Tender'
      }
    </div>);
  }

  projectLink(navigate) {
    return (project) => (<div data-step={this.showDataStep()?10:""}
                              data-intro=
                                {this.showDataStep()?"Click the Project Title to view project " +
                                  "details outlined in the approved cabinet paper.":""}>
      {
        project !== undefined
          ? <a href={`#!/tender/p/${project._id}`} onClick={() => navigate('p', project._id)}
               className="more-details-link">
            {project.projectTitle && project.projectTitle.toUpperCase()}
          </a>
          : 'No Project'
      }
    </div>);
  }

  downloadFiles(tender) {
    return <FileDownloadLinks files={(tender || {}).formDocs} />
  }

  linksAndFiles() {
    const { isFeatureVisible } = this.props;
    return (tender) => <div data-step={this.showDataStep()?11:""}
                            data-intro={this.showDataStep()?"Click to download the tender document hardcopy.":""}>
      {isFeatureVisible('publicView.tendersProcessList.tenderDocs')
      && this.downloadFiles(tender)}

      {isFeatureVisible('publicView.tendersProcessList.tenderLink')
      && this.downloadLinks(tender)}
    </div>;
   }

  downloadLinks(tender) {
    return tender && tender.tenderLink
      ? (<a className="download-file" href={tender.tenderLink} target="_blank">{tender.tenderLink}</a>)
      : null;
  }
  render() {
    const { data, count } = this.state;
    const { navigate, route, isFeatureVisible } = this.props;
    const [navigationPage, id] = route;
    this.introjsCnt = 0;

    const columns = [{
      title: 'Tender Title',
      dataField: 'tender',
      dataFormat: this.tenderLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.tenderTitle')
    }, {
      title: 'Department',
      dataField: 'department',
      visible: isFeatureVisible('publicView.tendersProcessList.department')
    }, {
      title: 'Fiscal Year',
      dataField: 'fiscalYear',
      visible: isFeatureVisible('publicView.tendersProcessList.fiscalYear')
    }, {
      title: 'Closing Date',
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? new Date(cell.closingDate).toLocaleDateString() : '',
      visible: isFeatureVisible('publicView.tendersProcessList.closingDate')
    }, {
      title: 'Tender Value',
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? this.props.styling.charts.hoverFormatter(cell.tenderValue) : '',
      visible: isFeatureVisible('publicView.tendersProcessList.tenderValue')
    }, {
      title: 'Project',
      dataField: 'project',
      dataFormat: this.projectLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.project')
    }, {
      title: 'Tender Documents',
      dataField: 'tender',
      dataFormat: this.linksAndFiles(),
      visible: isFeatureVisible('publicView.tendersProcessList.tenderLink')
        || isFeatureVisible('publicView.tendersProcessList.tenderDocs')
    }];

    return (<div className="container-fluid dashboard-default">

      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="tender"/>

      <div className="makueni-tenders content row" >
        <div className="col-md-3 col-sm-12 filters">
          <div className="row" data-intro="On each page there is a set of filters that allows you to
           limit what information is shown on the page by selected metrics, such as a specific
           location or department." data-step="8">
            <div className="filters-hint col-md-12">
              {this.t('filters:hint')}
            </div>
            {this.filters()}
          </div>
        </div>

        <div className="col-md-9 col-sm-12 col-main-content">
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
                  columns={columns.filter(c => c.visible)}
                />
              </div>
              : navigationPage === 't'
              ? <PurchaseReqView id={id} navigate={navigate}
                                 onSwitch={this.props.onSwitch}
                                 translations={this.props.translations}
                                 styling={this.props.styling}/>
              : <Project id={id} navigate={navigate} translations={this.props.translations}
                         styling={this.props.styling}/>
          }
        </div>
      </div>

      <div className="alerts-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('alerts')}>Subscribe to Email Alerts
            </button>
          </div>
        </div>
      </div>
      <div className="smshelp-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('smshelp')}>SMS Guide
            </button>
          </div>
        </div>
      </div>
      <Footer/>
    </div>);
  }
}

export default fmConnect(MakueniTenders);
