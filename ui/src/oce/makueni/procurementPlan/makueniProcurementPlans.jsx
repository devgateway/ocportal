import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import '../makueni.scss';
import ProcurementPlan from './single/procurementPlan';
import React, {useEffect, useState} from 'react';
import Footer from '../../layout/footer';
import fmConnect from "../../fm/fm";
import FileDownloadLinks from "../tenders/single/FileDownloadLinks";
import FiltersProcurementPlanWrapper from "../filters/FiltersProcurementPlanWrapper";
import {tCreator} from "../../translatable";
import {getProcurementPlans} from "../../api/Api";
import PropTypes from "prop-types";
import {useImmer} from "use-immer";

const MakueniProcurementPlans = props => {

  useEffect(() => window.scrollTo(0, 0));

  const [state, updateState] = useImmer({
    filters: {},
    page: 1,
    pageSize: 20,
    data: [],
    count: undefined
  });

  const setPage = page => updateState(draft => {
    draft.page = page;
  });

  const setPageSize = pageSize => updateState(draft => {
    draft.pageSize = pageSize
  });

  const setFilters = filters => updateState(draft => {
    draft.filters = filters;
    draft.page = 1;
  });

  useEffect(() => {
    const params = {
      ...state.filters,
      pageSize: state.pageSize,
      pageNumber: state.page - 1
    };

    getProcurementPlans(params).then(result => {
      updateState(draft => {
        draft.data =  result.data;
        draft.count = result.count;
      });
    });

  }, [state.filters, state.page, state.pageSize]);

  const filters = () =>
    <FiltersProcurementPlanWrapper
      filters={state.filters} applyFilters={setFilters} translations={props.translations}/>;

  const t = tCreator(props.translations);

  const ppLink = navigate => (ppId, data, formatExtraData, r) =>
    (<a data-intro={r === 0 ? t("tables:procurementPlans:clickForDetails") : ""}
        data-step={r === 0 ? 9 : ""}
        href={`#!/procurement-plan/pp/${ppId}`} onClick={() => navigate('pp', ppId)}
        className="more-details-link">
      {t("tables:procurementPlans:moreDetails")}
    </a>);

  const downloadFiles = () => (formDocs, data, formatExtraData, r) =>
    <div
      data-step={r === 0 ? 10 : ""}
      data-intro={r === 0 ? t("tables:procurementPlans:downloadFile:dataIntro") : ""}>

      <FileDownloadLinks files={formDocs} />
    </div>;

  const {navigate, route, isFeatureVisible} = props;
  const [navigationPage, id] = route;

  const columns = [{
    title: t("tables:procurementPlans:col:id"),
    dataField: 'id',
    width: '20%',
    dataFormat: ppLink(navigate),
    fm: 'publicView.procurementPlansList.id'
  }, {
    title: t("tables:procurementPlans:col:dpt"),
    dataField: 'department',
    fm: 'publicView.procurementPlansList.department'
  }, {
    title: t("tables:procurementPlans:col:fy"),
    dataField: 'fiscalYear',
    fm: 'publicView.procurementPlansList.fiscalYear'
  }, {
    title: t("tables:procurementPlans:col:ppf"),
    dataField: 'formDocs',
    dataFormat: downloadFiles(),
    fm: 'publicView.procurementPlansList.formDocs'
  }];

  const visibleColumns = columns.filter(c => isFeatureVisible(c.fm));

  return (<div className="container-fluid dashboard-default">

    <Header translations={props.translations} onSwitch={props.onSwitch}
            styling={props.styling} selected="procurement-plan"/>

    <div className="makueni-procurement-plan content row">
      <div className="col-md-3 col-sm-3 filters">
        <div className="row" data-intro={t("tables:procurementPlans:dataIntro")} data-step="8">
          <div className="filters-hint col-md-12">
            {t('filters:hint')}
          </div>
          {filters()}
        </div>
      </div>

      <div className="col-md-9 col-sm-9 col-main-content">
        {
          navigationPage === undefined
            ? <div>
              <h1>{t("tables:procurementPlans:title")}</h1>

              <BootstrapTableWrapper
                bordered
                data={state.data}
                page={state.page}
                pageSize={state.pageSize}
                onPageChange={setPage}
                onSizePerPageList={setPageSize}
                count={state.count}
                columns={visibleColumns}
              />
            </div>
            :
            <ProcurementPlan id={id} navigate={navigate} translations={props.translations}
                             styling={props.styling}/>
        }
      </div>
    </div>

    <div className="alerts-container">
      <div className="row alerts-button subscribe">
        <div className="col-md-12">
          <button className="btn btn-info btn-lg" type="submit"
                  onClick={() => props.onSwitch('alerts')}>
            {t("general:subscribeToEmailAlerts")}
          </button>
        </div>
      </div>
    </div>

    <div className="smshelp-container">
      <div className="row alerts-button subscribe">
        <div className="col-md-12">
          <button className="btn btn-info btn-lg" type="submit"
                  onClick={() => props.onSwitch('smshelp')}>
            {t("general:smsFeedbackHelp")}
          </button>
        </div>
      </div>
    </div>
    <Footer translations={props.translations}/>
  </div>);
}

MakueniProcurementPlans.propTypes = {
  styling: PropTypes.object.isRequired
};

export default fmConnect(MakueniProcurementPlans);
