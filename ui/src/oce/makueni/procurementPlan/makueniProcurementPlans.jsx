import CRDPage from '../../corruption-risk/page';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import { page, pageSize, ppCountRemote, ppData, ppFilters } from './state';
import { Map } from 'immutable';
import '../makueni.scss';
import ProcurementPlan from './single/procurementPlan';
import React from 'react';
import Footer from '../../layout/footer';
import fmConnect from "../../fm/fm";
import FileDownloadLinks from "../tenders/single/FileDownloadLinks";
import FiltersProcurementPlanWrapper from "../filters/FiltersProcurementPlanWrapper";

const NAME = 'MakueniPP';

class MakueniProcurementPlans extends CRDPage {

  constructor(props) {
    super(props);
    this.introjsCnt = 0;

    this.state = {
      data: []
    };
  }

  showDataStep() {
    return this.introjsCnt++ < 4;
  }

  componentDidMount() {
    super.componentDidMount();

    ppData.addListener(NAME, () => this.updateBindings());
    page.addListener(NAME, () => this.updateBindings());
    pageSize.addListener(NAME, () => this.updateBindings());
    ppCountRemote.addListener(NAME, () => this.updateBindings());
  }

  componentWillUnmount() {
    // reset all filters when we unmount this component
    ppFilters.assign('NAME', Map());

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

  resetPage() {
    page.assign(NAME, 1);
  }

  filters() {
    return <FiltersProcurementPlanWrapper
      filters={ppFilters} resetPage={this.resetPage.bind(this)} translations={this.props.translations}/>;
  }

  ppLink(navigate) {
    return (ppId) => (
      <a data-intro={this.showDataStep()? this.t("tables:procurementPlans:clickForDetails"):""}
         data-step={this.showDataStep()?9:""}
         href={`#!/procurement-plan/pp/${ppId}`} onClick={() => navigate('pp', ppId)}
         className="more-details-link">
        {this.t("tables:procurementPlans:moreDetails")}
      </a>
    );
  }

  downloadFiles() {
    return (formDocs) =>
      <FileDownloadLinks
        files={formDocs}
        data-step={this.showDataStep()?10:""}
        data-intro={this.showDataStep()?this.t("tables:procurementPlans:downloadFile:dataIntro"):""} />;
  }

  render() {
    const { data, count } = this.state;
    const { navigate, route, isFeatureVisible } = this.props;
    const [navigationPage, id] = route;
    this.introjsCnt = 0;

    const columns = [{
      title: this.t("tables:procurementPlans:col:id"),
      dataField: 'id',
      width: '20%',
      dataFormat: this.ppLink(navigate),
      fm: 'publicView.procurementPlansList.id'
    }, {
      title: this.t("tables:procurementPlans:col:dpt"),
      dataField: 'department',
      fm: 'publicView.procurementPlansList.department'
    }, {
      title: this.t("tables:procurementPlans:col:fy"),
      dataField: 'fiscalYear',
      fm: 'publicView.procurementPlansList.fiscalYear'
    }, {
      title: this.t("tables:procurementPlans:col:ppf"),
      dataField: 'formDocs',
      dataFormat: this.downloadFiles(),
      fm: 'publicView.procurementPlansList.formDocs'
    }];

    const visibleColumns = columns.filter(c => isFeatureVisible(c.fm));

    return (<div className="container-fluid dashboard-default">

      <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
              styling={this.props.styling} selected="procurement-plan"/>

      <div className="makueni-procurement-plan content row">
        <div className="col-md-3 col-sm-3 filters">
          <div className="row" data-intro={this.t("tables:procurementPlans:dataIntro")} data-step="8">
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
                <h1>{this.t("tables:procurementPlans:title")}</h1>

                <BootstrapTableWrapper
                  bordered
                  data={data}
                  page={this.state.page}
                  pageSize={this.state.pageSize}
                  onPageChange={newPage => page.assign(NAME, newPage)}
                  onSizePerPageList={newPageSize => pageSize.assign(NAME, newPageSize)}
                  count={count}
                  columns={visibleColumns}
                />
              </div>
              :
              <ProcurementPlan id={id} navigate={navigate} translations={this.props.translations}
                               styling={this.props.styling}/>
          }
        </div>
      </div>

      <div className="alerts-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('alerts')}>
              {this.t("general:subscribeToEmailAlerts")}
            </button>
          </div>
        </div>
      </div>

      <div className="smshelp-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button className="btn btn-info btn-lg" type="submit"
                    onClick={() => this.props.onSwitch('smshelp')}>
              {this.t("general:smsFeedbackHelp")}
            </button>
          </div>
        </div>
      </div>
      <Footer translations={this.props.translations}/>
    </div>);
  }
}

export default fmConnect(MakueniProcurementPlans);
