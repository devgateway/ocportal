import TopSearch from '../../top-search';
import { tCreator } from '../../../translatable';
import Info from './info';
import Zoomable from '../../zoomable';
import TitleBelow from '../../archive/title-below';
import WinsAndFlags from '../../bars/wins-and-flags';
import FlaggedNr from '../../bars/flagged-nr';
import ProcurementsByStatus from '../../bars/by-status';
import ProcurementsByMethod from '../../bars/by-method';
import ProcurementsTable from '../../table/procurements';
import './style.scss';
import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { createSelector } from '@reduxjs/toolkit';
import { fetchAllInfo } from './api';

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
  translations, doSearch, width, navigate, ...otherProps
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

  const t = tCreator(translations);

  return (
    <div className="pe-page single-page">
      <TopSearch
        translations={translations}
        doSearch={doSearch}
        placeholder={t('crd:procuringEntities:top-search')}
      />
      {state
      && (
      <>
        <Info
          info={state.info}
          flagsCount={state.flagsCount}
          buyers={state.buyers}
          contractsCount={state.contractsCount}
          unflaggedContractsCount={state.unflaggedContractsCount}
          translations={translations}
        />
        <section className="pe-general-statistics">
          <h2>{t('crd:procuringEntities:generalStatistics')}</h2>
          <div className="row">
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:procuringEntities:byStatus:title')}>
                  <ProcurementsByStatus
                    data={state.procurementsByStatusData}
                    length={state.maxCommonDataLength}
                    translations={translations}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:procuringEntities:byMethod:title')}>
                  <ProcurementsByMethod
                    data={state.procurementsByMethodData}
                    length={state.maxCommonDataLength}
                    translations={translations}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
          </div>
        </section>
        <section className="flag-analysis">
          <h2>
            {t('crd:contracts:flagAnalysis')}
          </h2>
          <div className="row">
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:procuringEntity:winsAndFlags:title')}>
                  <WinsAndFlags
                    data={state.winsAndFlagsData}
                    length={state.max2ndRowCommonDataLength}
                    translations={translations}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:procuringEntity:flaggedNr:title')}>
                  <FlaggedNr
                    data={state.flaggedNrData}
                    length={state.max2ndRowCommonDataLength}
                    translations={translations}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
          </div>
        </section>
      </>
      )}
      <section>
        {state && (
        <h2>
          Procurements by
          {state.info.name}
        </h2>
        )}
        <ProcurementsTable
          filters={peFilters}
          translations={translations}
          navigate={navigate}
        />
      </section>
    </div>
  );
};

ProcuringEntity.propTypes = {
  id: PropTypes.string.isRequired,
  filters: PropTypes.object,
  years: PropTypes.array,
  months: PropTypes.array,
};

export default ProcuringEntity;
