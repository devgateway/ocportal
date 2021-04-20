import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { createSelector } from '@reduxjs/toolkit';
import { useImmer } from 'use-immer';
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

const buyerFiltersSelector = createSelector(
  [(props) => props.id, (props) => props.filters, (props) => props.years, (props) => props.months],
  (id, filters, years, months) => ({
    ...filters,
    year: years,
    month: months,
    buyerId: id,
  }),
);

const Buyer = ({
  doSearch, width, isFeatureVisible, ...otherProps
}) => {
  useEffect(() => window.scrollTo(0, 0), []);

  const [state, updateState] = useImmer();

  const buyerFilters = buyerFiltersSelector(otherProps);

  useEffect(() => {
    fetchAllInfo(buyerFilters)
      .then(
        (data) => {
          updateState(() => ({
            ...data,
            maxCommonDataLength: Math.min(5,
              Math.max(data.procurementsByStatusData.length, data.procurementsByMethodData.length)),
            max2ndRowCommonDataLength: Math.min(5,
              Math.max(data.winsAndFlagsData.length, data.flaggedNrData.length)),
          }));
        },
        () => updateState(() => undefined),
      );
  }, [buyerFilters]);

  const { t } = useTranslation();

  return (
    <div className="pe-page single-page">
      <TopSearch
        t={t}
        doSearch={doSearch}
        placeholder={t('crd:buyers:top-search')}
      />
      {state
      && (
      <>
        {isFeatureVisible('crd.buyer.info')
        && (
          <Info
            info={state.info}
            flagsCount={state.flagsCount}
            prs={state.prs}
            contractsCount={state.contractsCount}
            unflaggedContractsCount={state.unflaggedContractsCount}
          />
        )}
        {isFeatureVisible('crd.buyer.statistics')
        && (
          <section className="pe-general-statistics">
            <h2>{t('crd:procuringEntities:generalStatistics')}</h2>
            <div className="row">
              {isFeatureVisible('crd.buyer.statistics.procurementsByStatus')
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
              {isFeatureVisible('crd.buyer.statistics.procurementsByMethod')
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
        {isFeatureVisible('crd.buyer.flagAnalysis')
        && (
          <section className="flag-analysis">
            <h2>
              {t('crd:contracts:flagAnalysis')}
            </h2>
            <div className="row">
              {isFeatureVisible('crd.buyer.flagAnalysis.winsAndFlags')
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
              {isFeatureVisible('crd.buyer.flagAnalysis.flaggedNr')
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
      {isFeatureVisible('crd.buyer.procurements')
      && (
        <section>
          {state && (
            <h2>
              {t('crd:procuringEntity:procurementsBy', { name: state.info.name })}
            </h2>
          )}
          <ProcurementsTable
            filters={buyerFilters}
            fmPrefix="crd.buyer.procurements.col"
          />
        </section>
      )}
    </div>
  );
};

Buyer.propTypes = {
  id: PropTypes.string.isRequired,
  filters: PropTypes.object,
  years: PropTypes.array,
  months: PropTypes.array,
};

export default fmConnect(Buyer);
