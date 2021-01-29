import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { createSelector } from '@reduxjs/toolkit';
import { useTranslation } from 'react-i18next';
import TopSearch from '../../top-search';
import Info from './info';
import Zoomable from '../../zoomable';
import TitleBelow from '../../archive/title-below';
import WinsAndFlags from '../../bars/wins-and-flags';
import FlaggedNr from '../../bars/flagged-nr';
import ProcurementsByStatus from '../../bars/by-status';
import ProcurementsByMethod from '../../bars/by-method';
import ProcurementsTable from '../../table/procurements';
import './style.scss';
import { fetchAllInfo } from './api';
import fmConnect from '../../../fm/fm';

const procuringEntityFiltersSelector = createSelector(
  [(props) => props.id, (props) => props.filters, (props) => props.years, (props) => props.months],
  (id, filters, years, months) => ({
    ...filters,
    year: years,
    month: months,
    procuringEntityId: id,
  }),
);

const ProcuringEntity = ({
  doSearch, width, navigate, isFeatureVisible, ...otherProps
}) => {
  useEffect(() => window.scrollTo(0, 0), []);

  const [state, setState] = useState();

  const peFilters = procuringEntityFiltersSelector(otherProps);

  useEffect(() => {
    fetchAllInfo(peFilters)
      .then(
        (state) => setState({
          ...state,
          maxCommonDataLength: Math.min(5,
            Math.max(state.procurementsByStatusData.length, state.procurementsByMethodData.length)),
          max2ndRowCommonDataLength: Math.min(5,
            Math.max(state.winsAndFlagsData.length, state.flaggedNrData.length)),
        }),
        () => setState(null),
      );
  }, [peFilters]);

  const { t } = useTranslation();

  return (
    <div className="pe-page single-page">
      <TopSearch
        t={t}
        doSearch={doSearch}
        placeholder={t('crd:procuringEntities:top-search')}
      />
      {state
      && (
      <>
        {isFeatureVisible('crd.procuringEntity.info')
        && (
          <Info
            info={state.info}
            flagsCount={state.flagsCount}
            buyers={state.buyers}
            contractsCount={state.contractsCount}
            unflaggedContractsCount={state.unflaggedContractsCount}
          />
        )}
        {isFeatureVisible('crd.procuringEntity.statistics')
        && (
          <section className="pe-general-statistics">
            <h2>{t('crd:procuringEntities:generalStatistics')}</h2>
            <div className="row">
              {isFeatureVisible('crd.procuringEntity.statistics.procurementsByStatus')
              && (
                <div className="col-sm-6">
                  <Zoomable zoomedWidth={width}>
                    <TitleBelow title={t('crd:procuringEntities:byStatus:title')}>
                      <ProcurementsByStatus
                        data={state.procurementsByStatusData}
                        length={state.maxCommonDataLength}
                      />
                    </TitleBelow>
                  </Zoomable>
                </div>
              )}
              {isFeatureVisible('crd.procuringEntity.statistics.procurementsByMethod')
              && (
                <div className="col-sm-6">
                  <Zoomable zoomedWidth={width}>
                    <TitleBelow title={t('crd:procuringEntities:byMethod:title')}>
                      <ProcurementsByMethod
                        data={state.procurementsByMethodData}
                        length={state.maxCommonDataLength}
                      />
                    </TitleBelow>
                  </Zoomable>
                </div>
              )}
            </div>
          </section>
        )}

        {isFeatureVisible('crd.procuringEntity.flagAnalysis')
        && (
          <section className="flag-analysis">
            <h2>
              {t('crd:contracts:flagAnalysis')}
            </h2>
            <div className="row">
              {isFeatureVisible('crd.procuringEntity.flagAnalysis.winsAndFlags')
              && (
                <div className="col-sm-6">
                  <Zoomable zoomedWidth={width}>
                    <TitleBelow title={t('crd:procuringEntity:winsAndFlags:title')}>
                      <WinsAndFlags
                        data={state.winsAndFlagsData}
                        length={state.max2ndRowCommonDataLength}
                      />
                    </TitleBelow>
                  </Zoomable>
                </div>
              )}
              {isFeatureVisible('crd.procuringEntity.flagAnalysis.flaggedNr')
              && (
                <div className="col-sm-6">
                  <Zoomable zoomedWidth={width}>
                    <TitleBelow title={t('crd:procuringEntity:flaggedNr:title')}>
                      <FlaggedNr
                        data={state.flaggedNrData}
                        length={state.max2ndRowCommonDataLength}
                      />
                    </TitleBelow>
                  </Zoomable>
                </div>
              )}
            </div>
          </section>
        )}
      </>
      )}
      {isFeatureVisible('crd.procuringEntity.procurements')
      && (
        <section>
          {state && (
            <h2>
              Procurements by
              {state.info.name}
            </h2>
          )}
          <ProcurementsTable
            filters={peFilters}
            navigate={navigate}
            fmPrefix="crd.procuringEntity.procurements.col"
          />
        </section>
      )}
    </div>
  );
};

ProcuringEntity.propTypes = {
  id: PropTypes.string.isRequired,
  filters: PropTypes.object,
  years: PropTypes.array,
  months: PropTypes.array,
};

export default fmConnect(ProcuringEntity);
