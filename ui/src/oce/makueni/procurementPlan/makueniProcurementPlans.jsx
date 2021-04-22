import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
import { Link, Route, Switch } from 'react-router-dom';
import Header from '../../layout/header';
import BootstrapTableWrapper from '../../corruption-risk/archive/bootstrap-table-wrapper';
import '../makueni.scss';
import ProcurementPlan from './single/procurementPlan';
import Footer from '../../layout/footer';
import FileDownloadLinks from '../tenders/single/FileDownloadLinks';
import FiltersProcurementPlanWrapper from '../filters/FiltersProcurementPlanWrapper';
import { getProcurementPlans } from '../../api/Api';
import fmConnect from '../../fm/fm';

const MakueniProcurementPlans = (props) => {
  const [state, updateState] = useImmer({
    filters: {},
    page: 1,
    pageSize: 20,
    data: [],
    count: 0,
  });

  const setPage = (page) => updateState((draft) => {
    draft.page = page;
  });

  const setPageSize = (pageSize) => updateState((draft) => {
    draft.pageSize = pageSize;
  });

  const setFilters = (filters) => updateState((draft) => {
    draft.filters = filters;
    draft.page = 1;
  });

  useEffect(() => {
    const params = {
      ...state.filters,
      pageSize: state.pageSize,
      pageNumber: state.page - 1,
    };

    getProcurementPlans(params).then((result) => {
      updateState((draft) => {
        draft.data = result.data;
        draft.count = result.count;
      });
    });
  }, [state.filters, state.page, state.pageSize]);

  const filters = () => (
    <FiltersProcurementPlanWrapper
      filters={state.filters}
      applyFilters={setFilters}
    />
  );

  const { t } = useTranslation();

  const ppLink = (ppId) => (
    <Link
      to={`/portal/procurement-plan/pp/${ppId}`}
      className="more-details-link"
    >
      {t('tables:procurementPlans:moreDetails')}
    </Link>
  );

  const downloadFiles = () => (formDocs, data, formatExtraData, r) => (
    <div
      data-step={r === 0 ? 10 : ''}
      data-intro={r === 0 ? t('tables:procurementPlans:downloadFile:dataIntro') : ''}
    >

      <FileDownloadLinks files={formDocs} />
    </div>
  );

  const { isFeatureVisible } = props;

  const columns = [{
    text: t('tables:procurementPlans:col:id'),
    dataField: 'id',
    headerStyle: {
      width: '20%',
    },
    formatter: ppLink,
    fm: 'publicView.procurementPlans.col.id',
  }, {
    text: t('tables:procurementPlans:col:dpt'),
    dataField: 'department',
    fm: 'publicView.procurementPlans.col.department',
  }, {
    text: t('tables:procurementPlans:col:fy'),
    dataField: 'fiscalYear',
    fm: 'publicView.procurementPlans.col.fiscalYear',
  }, {
    text: t('tables:procurementPlans:col:ppf'),
    dataField: 'formDocs',
    formatter: downloadFiles(),
    fm: 'publicView.procurementPlans.col.formDocs',
  }];

  return (
    <div className="container-fluid dashboard-default">

      <Header
        onSwitch={props.onSwitch}
        styling={props.styling}
        selected="procurement-plan"
      />

      <div className="makueni-procurement-plan content row">
        <div className="col-md-3 col-sm-3 filters">
          <div className="row" data-intro={t('tables:procurementPlans:dataIntro')} data-step="8">
            <div className="filters-hint col-md-12">
              {t('filters:hint')}
            </div>
            {filters()}
          </div>
        </div>

        <div className="col-md-9 col-sm-9 col-main-content">
          <Switch>
            <Route exact path="/portal/procurement-plan">
              <div>
                <h1>{t('tables:procurementPlans:title')}</h1>
                <BootstrapTableWrapper
                  data={state.data}
                  page={state.page}
                  pageSize={state.pageSize}
                  onPageChange={setPage}
                  onSizePerPageList={setPageSize}
                  count={state.count}
                  columns={columns}
                />
              </div>
            </Route>
            <Route path="/portal/procurement-plan/pp/:id">
              <ProcurementPlan
                styling={props.styling}
              />
            </Route>
          </Switch>
        </div>
      </div>
      {isFeatureVisible('publicView.subscribeToAlertsButton')
      && (
      <div className="alerts-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button
              className="btn btn-info btn-lg"
              type="submit"
              onClick={() => props.onSwitch('alerts')}
            >
              {t('general:subscribeToEmailAlerts')}
            </button>
          </div>
        </div>
      </div>
      )}
      {isFeatureVisible('publicView.smsHelpButton')
      && (
      <div className="smshelp-container">
        <div className="row alerts-button subscribe">
          <div className="col-md-12">
            <button
              className="btn btn-info btn-lg"
              type="submit"
              onClick={() => props.onSwitch('smshelp')}
            >
              {t('general:smsFeedbackHelp')}
            </button>
          </div>
        </div>
      </div>
      )}
      <Footer />
    </div>
  );
};

MakueniProcurementPlans.propTypes = {
  styling: PropTypes.object.isRequired,
};

export default fmConnect(MakueniProcurementPlans);
