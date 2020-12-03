import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { mtFilters, page, pageSize, tendersCountRemote, tendersData } from './state';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { Map } from 'immutable';

import '../makueni.scss';
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
    data-intro={this.showDataStep()?this.t("tables:tenderLink:dataIntro"):""}>
      {
        tender !== undefined
          ? <a href={`#!/tender/t/${tender.purchaseReqId}`}
               onClick={() => navigate('t', tender.purchaseReqId)} className="more-details-link">
            {tender.tenderTitle && tender.tenderTitle.toUpperCase()}
          </a>
          : this.t("tables:tenderLink:noTender")
      }
    </div>);
  }

  projectLink(navigate) {
    return (project) => (<div data-step={this.showDataStep()?10:""}
                              data-intro=
                                {this.showDataStep()?this.t("tables:projectLink:dataIntro"):""}>
      {
        project !== undefined
          ? <a href={`#!/tender/p/${project._id}`} onClick={() => navigate('p', project._id)}
               className="more-details-link">
            {project.projectTitle && project.projectTitle.toUpperCase()}
          </a>
          : this.t("tables:projectLink:noProject")
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
      title: this.t("tables:tenders:col:title"),
      dataField: 'tender',
      dataFormat: this.tenderLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.tenderTitle')
    }, {
      title: this.t("tables:tenders:col:department"),
      dataField: 'department',
      visible: isFeatureVisible('publicView.tendersProcessList.department')
    }, {
      title: this.t("tables:tenders:col:fiscalYear"),
      dataField: 'fiscalYear',
      visible: isFeatureVisible('publicView.tendersProcessList.fiscalYear')
    }, {
      title: this.t("tables:tenders:col:closingDate"),
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? new Date(cell.closingDate).toLocaleDateString() : '',
      visible: isFeatureVisible('publicView.tendersProcessList.closingDate')
    }, {
      title: this.t("tables:tenders:col:tenderValue"),
      dataField: 'tender',
      dataFormat: (cell, row) => cell !== undefined ? this.props.styling.charts.hoverFormatter(cell.tenderValue) : '',
      visible: isFeatureVisible('publicView.tendersProcessList.tenderValue')
    }, {
      title: this.t("tables:tenders:col:project"),
      dataField: 'project',
      dataFormat: this.projectLink(navigate),
      visible: isFeatureVisible('publicView.tendersProcessList.project')
    }, {
      title: this.t("tables:tenders:col:documents"),
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
          <div className="row" data-intro={this.t("tables:tenders:filters:dataIntro")} data-step="8">
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
                <h1>{this.t("tables:tenders:title")}</h1>

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
                    onClick={() => this.props.onSwitch('alerts')}>{this.t("general:subscribeToEmailAlerts")}
            </button>
          </div>
        </div>
      </div>
      <div className="smshelp-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('smshelp')}>{this.t("general:smsFeedbackHelp")}
            </button>
          </div>
        </div>
      </div>
      <Footer translations={this.props.translations}/>
    </div>);
  }
}

export default fmConnect(MakueniTenders);
