import React, { useEffect, useState } from 'react';
import { List } from 'immutable';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import TopSearch from '../../top-search';
import Visualization from '../../../visualization';
import { wirePropsPlain } from '../../tools';
import NrLostVsWon from './donuts/nr-lost-vs-won';
import AmountLostVsWon from './donuts/amount-lost-vs-won';
import NrFlags from './donuts/nr-flags';
import './style.scss';
import { cacheFn } from '../../../tools';
import Zoomable from '../../zoomable';
import Crosstab from '../../clickable-crosstab';
import { CORRUPTION_TYPES } from '../../constants';
import FlaggedNr from '../../bars/flagged-nr';
import BackendDateFilterable from '../../backend-date-filterable';
import WinsAndFlags from '../../bars/wins-and-flags/index';
import SupplierTable from './table';
import TitleBelow from '../../archive/title-below';
import { fetchAllInfo } from './api';
import flag from '../../../resources/icons/flag.svg';
import fmConnect from '../../../fm/fm';

class CrosstabExplanation extends React.PureComponent {
  render() {
    const { nrFlags, corruptionType, t } = this.props;
    return (
      <p>
        {t('crd:supplier:crosstabs:explanation')
          .replace('$#$', nrFlags)
          .replace('$#$', t(`crd:corruptionType:${corruptionType}:pageTitle`))}
      </p>
    );
  }
}

CrosstabExplanation.propTypes = {
  t: PropTypes.func.isRequired,
};

class Info extends Visualization {
  getCustomEP() {
    const { id } = this.props;
    return [
      `ocds/organization/supplier/id/${id}`,
      `totalFlags?supplierId=${id}`,
      `ocds/release/count?supplierId=${id}`,
    ];
  }

  transform([info, _totalFlags, totalContracts]) {
    let totalFlags = 0;
    try {
      totalFlags = _totalFlags[0].flaggedCount;
    } catch (e) {
      console.log('Total flags fetching failed', e);
    }
    return {
      info,
      totalFlags,
      totalContracts,
    };
  }

  render() {
    const { data, t } = this.props;

    if (!data) return null;

    const info = data.get('info');
    const flagCount = data.get('totalFlags');
    const contractCount = data.get('totalContracts');

    const address = info.get('address');
    return (
      <section className="info">
        <table className="table table-bordered join-bottom info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>{t('crd:supplier:ID')}</dt>
                  <dd>{info.get('id')}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{t('crd:supplier:name')}</dt>
                  <dd>{info.get('name')}</dd>
                </dl>
              </td>
              <td className="flags">
                <img src={flag} alt="Flag icon" className="flag-icon" />
                &nbsp;
                <span className="count">
                  {flagCount}
                  &nbsp;
                  {t(flagCount === 1
                    ? 'crd:contracts:baseInfo:flag:sg'
                    : 'crd:contracts:baseInfo:flag:pl')}
                </span>
                <small>
                  (
                  {contractCount}
                    &nbsp;
                  {t(contractCount === 1
                    ? 'crd:supplier:contract:sg'
                    : 'crd:supplier:contract:pl')}
                  )
                </small>
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered info-table">
          <tbody>
            <tr>
              <td>
                <dl className="smaller">
                  <dt>{t('crd:supplier:address')}</dt>
                  {address && (
                  <dd>
                    {address.get('streetAddress')}
                    <br />
                    {address.get('locality')}
                    {' '}
                    /
                    &nbsp;
                    {address.get('postalCode')}
                    {' '}
                    /
                    &nbsp;
                    {address.get('countryName')}
                  </dd>
                  )}
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

const injectSupplierFilter = cacheFn((filters, supplierId) => ({
  ...filters,
  supplierId: (filters.supplierId || []).concat(supplierId),
}));

const injectDatefulFilter = cacheFn((filters, years, months) => ({
  ...filters,
  year: years,
  month: months,
}));

const injectBidderFilter = cacheFn((filters, bidderId) => ({
  ...filters,
  bidderId: (filters.bidderId || []).concat(bidderId),
}));

const groupIndicators = cacheFn((indicatorTypesMapping) => {
  const result = {};
  CORRUPTION_TYPES.forEach((corruptionType) => { result[corruptionType] = []; });
  if (indicatorTypesMapping) {
    Object.keys(indicatorTypesMapping).forEach((indicatorName) => {
      const indicator = indicatorTypesMapping[indicatorName];
      indicator.types.forEach((type) => result[type].push(indicatorName));
    });
  }
  return result;
});

const Supplier = (props) => {
  useEffect(() => window.scrollTo(0, 0), []);

  const {
    doSearch, id, filters, years, months, data, isFeatureVisible,
  } = props;

  const { t } = useTranslation();

  const totalFlags = data.getIn(['info', 'totalFlags']);

  const supplierFilter = injectSupplierFilter(filters, id);

  const supplierDatefulFilter = injectDatefulFilter(supplierFilter, years, months);

  const [flagRowState, setFlagRowState] = useState();

  useEffect(() => {
    if (totalFlags) {
      fetchAllInfo(supplierDatefulFilter)
        .then(
          (s) => setFlagRowState({
            ...s,
            maxCommonDataLength: Math.min(5,
              Math.max(s.winsAndFlagsData.length, s.flaggedNrData.length)),
          }),
          () => setFlagRowState(null),
        );
    } else {
      setFlagRowState(null);
    }
  }, [totalFlags, supplierDatefulFilter]);

  const maybeGetFlagAnalysis = () => {
    const { indicatorTypesMapping, requestNewData } = props;

    const nrFlagsByCorruptionType = {};
    CORRUPTION_TYPES.forEach((corruptionType) => { nrFlagsByCorruptionType[corruptionType] = 0; });
    data.get('nr-flags', List()).forEach((corruptionType) => {
      nrFlagsByCorruptionType[corruptionType.get('type')] = corruptionType.get('indicatorCount');
    });

    const indicators = groupIndicators(indicatorTypesMapping);
    const noIndicators = Object
      .keys(nrFlagsByCorruptionType)
      .every((key) => nrFlagsByCorruptionType[key] === 0);

    if (noIndicators) {
      return (
        <section className="flag-analysis">
          <h4>{t('crd:supplier:noFlags')}</h4>
        </section>
      );
    }

    return (
      <section className="flag-analysis">
        <br />
        {isFeatureVisible('crd.supplier.flagAnalysis')
        && isFeatureVisible('crd.supplier.flagAnalysis.crosstab')
        && CORRUPTION_TYPES
          .filter((corruptionType) => nrFlagsByCorruptionType[corruptionType])
          .map((corruptionType) => (
            <div key={corruptionType}>
              <h3>
                {t(`crd:corruptionType:${corruptionType}:pageTitle`)}
              </h3>
              <CrosstabExplanation
                t={t}
                corruptionType={corruptionType}
                nrFlags={nrFlagsByCorruptionType[corruptionType]}
              />
              <Crosstab
                {...wirePropsPlain(props, ['crosstab', corruptionType])}
                filters={injectSupplierFilter(filters, id)}
                requestNewData={(path, newData) => {
                  const toRemove = newData.filter((row) => row.every((cell) => cell.get('count') === 0)).keySeq();
                  requestNewData(path.concat(['crosstab', corruptionType]),
                    newData.withMutations((data) => {
                      toRemove.forEach((indicator) => {
                        data.delete(indicator);
                        data.keySeq().forEach((key) => {
                          data.deleteIn([key, indicator]);
                        });
                      });
                    }));
                }}
                indicators={indicators[corruptionType]}
                showRawNumbers
              />
            </div>
          ))}
        {isFeatureVisible('crd.supplier.procurements')
        && (
          <>
            <h2>{t('crd:supplier:table:procurementsWon')}</h2>
            <SupplierTable filters={supplierDatefulFilter} />
          </>
        )}
      </section>
    );
  };

  const maybeGetSections = () => {
    const { width, styling } = props;
    const donutSize = width / 3 - window.innerWidth / 20;
    return (
      <div>
        {isFeatureVisible('crd.supplier.statistics')
        && (
          <section className="supplier-general-statistics">
            <h2>{t('crd:supplier:generalStatistics')}</h2>
            {isFeatureVisible('crd.supplier.statistics.nrLostVsWon')
            && (
              <div className="col-sm-4">
                <NrLostVsWon
                  {...wirePropsPlain(props, 'nr-lost-vs-won')}
                  filters={injectBidderFilter(filters, id)}
                  width={donutSize}
                  styling={styling}
                />
              </div>
            )}
            {isFeatureVisible('crd.supplier.statistics.amountLostVsWon')
            && (
              <div className="col-sm-4">
                <AmountLostVsWon
                  {...wirePropsPlain(props, 'amount-lost-vs-won')}
                  filters={injectBidderFilter(filters, id)}
                  width={donutSize}
                  styling={styling}
                />
              </div>
            )}
            {isFeatureVisible('crd.supplier.statistics.nrFlags')
            && (
              <div className="col-sm-4">
                <NrFlags
                  {...wirePropsPlain(props, 'nr-flags')}
                  filters={injectSupplierFilter(filters, id)}
                  width={donutSize}
                  styling={styling}
                />
              </div>
            )}
          </section>
        )}
        {flagRowState && isFeatureVisible('crd.supplier.flagAnalysis')
        && (
        <section className="flag-analysis">
          <h2>
            {t('crd:contracts:flagAnalysis')}
          </h2>
          {isFeatureVisible('crd.supplier.flagAnalysis.winsAndFlags')
          && (
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:supplier:winsAndLosses:title')}>
                  <WinsAndFlags
                    data={flagRowState.winsAndFlagsData}
                    length={flagRowState.maxCommonDataLength}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
          )}
          {isFeatureVisible('crd.supplier.flagAnalysis.flaggedNr')
          && (
            <div className="col-sm-6">
              <Zoomable zoomedWidth={width}>
                <TitleBelow title={t('crd:supplier:flaggedNr:title')}>
                  <FlaggedNr
                    data={flagRowState.flaggedNrData}
                    length={flagRowState.maxCommonDataLength}
                  />
                </TitleBelow>
              </Zoomable>
            </div>
          )}
        </section>
        )}
        {maybeGetFlagAnalysis()}
      </div>
    );
  };

  return (
    <div className="supplier-page">
      <TopSearch
        t={t}
        doSearch={doSearch}
        placeholder={t('crd:suppliers:top-search')}
      />
      {isFeatureVisible('crd.supplier.info')
      && (
        <BackendDateFilterable {...wirePropsPlain(props, 'info')}>
          <Info
            id={id}
            filters={{}}
            requestNewData={() => { }}
          />
        </BackendDateFilterable>
      )}
      {totalFlags === 0 && (
      <section className="flag-analysis">
        <h2>{t('crd:contracts:flagAnalysis')}</h2>
        <h4>This supplier has no flags</h4>
      </section>
      )}

      {!!totalFlags && maybeGetSections()}
    </div>
  );
};

Supplier.propTypes = {
  width: PropTypes.number.isRequired,
  doSearch: PropTypes.func.isRequired,
  id: PropTypes.string.isRequired,
  filters: PropTypes.object.isRequired,
  years: PropTypes.arrayOf(PropTypes.number),
  months: PropTypes.arrayOf(PropTypes.number),
  data: PropTypes.object,
  styling: PropTypes.object.isRequired,
  indicatorTypesMapping: PropTypes.object.isRequired,
  isFeatureVisible: PropTypes.func.isRequired,
  requestNewData: PropTypes.func.isRequired,
};

export default fmConnect(Supplier);
