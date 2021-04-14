import React from 'react';
import { Map, List } from 'immutable';
import PropTypes from 'prop-types';
import CRDPage from '../../page';
import Visualization from '../../../visualization';
import TopSearch from '../../top-search';
import NrOfBidders from './donuts/nr-of-bidders';
import NrOfContractsWithThisPE from './donuts/nr-contract-with-pe';
import PercentPESpending from './donuts/percent-pe-spending';
import PercentPESpendingPopup from './donuts/percent-pe-spending/popup';
import Crosstab from '../../clickable-crosstab';
import CustomPopup from '../../custom-popup';
import DonutPopup from '../../donut/popup';
import { wireProps } from '../../tools';
import '../style.scss';
import DataFetcher from '../../data-fetcher';
import { cacheFn } from '../../../tools';
import flag from '../../../resources/icons/flag.svg';
import fmConnect from '../../../fm/fm';

class CrosstabExplanation extends React.PureComponent {
  render() {
    const {
      data, totalContracts, nrFlags, corruptionType, t,
    } = this.props;
    const template = nrFlags === 1
      ? t('crd:contracts:crosstab:explanation:sg')
      : t('crd:contracts:crosstab:explanation:pl');

    return (
      <p>
        {template.replace('$#$', data)
          .replace('$#$', ((data / totalContracts) * 100).toFixed(2))
          .replace('$#$', nrFlags)
          .replace(
            '$#$',
            t(`crd:corruptionType:${corruptionType}:name`)
              .toLowerCase(),
          )}
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
    return `flaggedRelease/ocid/${id}`;
  }

  render() {
    const {
      data, supplier, gotoSupplier, t,
    } = this.props;

    const title = data.getIn(['tender', 'title']);
    const startDate = data.getIn(['tender', 'tenderPeriod', 'startDate']);
    const endDate = data.getIn(['tender', 'tenderPeriod', 'endDate']);
    const award = data.get('awards', List()).find((a) => a.get('status') === 'active') || Map();

    const PE = data.getIn(['tender', 'procuringEntity']);

    const flagCount = data.get('flags', List()).filter((flag) => flag.get && flag.get('value')).count();
    return (
      <section className="info">
        <div className="row">
          <dl className="col-md-4">
            <dt><span className="contract-label">{t('crd:procurementsTable:contractID')}</span></dt>
            <dd><span className="contract-value">{data.get('ocid')}</span></dd>
          </dl>
          <dl className="col-md-4">
            <dt><span className="contract-label">{t('crd:contracts:baseInfo:status')}</span></dt>
            <dd><span className="contract-value">{data.get('tag', []).join(', ')}</span></dd>
          </dl>
          <div className="col-md-4 flags">
            <img src={flag} alt="Flag icon" className="flag-icon" />
            &nbsp;
            <span className="count">
              {flagCount}
              &nbsp;
              {t(flagCount === 1
                ? 'crd:contracts:baseInfo:flag:sg'
                : 'crd:contracts:baseInfo:flag:pl')}
            </span>
          </div>
        </div>
        {title
          && (
          <dl>
            {title && <dt>{t('crd:general:contract:title')}</dt>}
            {title && <dd>{title}</dd>}
          </dl>
          )}
        <table className="table table-bordered table-fixed join-bottom info-table">
          <tbody>
            <tr>
              {PE && (
                <td>
                  <dl>
                    <dt><span className="contract-label">{t('crd:contracts:baseInfo:procuringEntityName')}</span></dt>
                    <dd>
                      <a
                        href={`#!/crd/procuring-entity/${PE.get('id')}`}
                      >
                        {PE.get('name')}
                      </a>
                    </dd>
                  </dl>
                </td>
              )}
              <td>
                <dl>
                  <dt><span className="contract-label">{t('crd:contracts:baseInfo:buyer')}</span></dt>
                  <dd><span className="contract-value">{data.getIn(['buyer', 'name'], t('general:undefined'))}</span></dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt><span className="contract-label">{t('crd:contracts:baseInfo:suppliers')}</span></dt>
                  <dd>
                    <span className="contract-value">
                      {supplier
                        ? (
                          <a
                            href={`#!/crd/supplier/${supplier.get('id')}`}
                            onClick={gotoSupplier}
                          >
                            {supplier.get('name')}
                          </a>
                        )
                        : t('general:undefined')}
                    </span>
                  </dd>
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered table-fixed">
          <tbody>
            <tr>
              <td>
                <span className="contract-label">{t('crd:contracts:baseInfo:tenderAmount')}</span>
                &nbsp;
                <span className="contract-value">
                  {data.getIn(['tender', 'value', 'amount'], t('general:undefined'))}
                  &nbsp;
                  {data.getIn(['tender', 'value', 'currency'])}
                </span>
              </td>
              <td>
                <span className="contract-label">{t('crd:contracts:baseInfo:tenderDates')}</span>
                &nbsp;
                <span className="contract-value">
                  {startDate
                    ? (
                      <span>
                        {new Date(startDate).toLocaleDateString()}
                        &ndash;
                      </span>
                    )
                    : t('general:undefined')}

                  {endDate && new Date(endDate).toLocaleDateString()}
                </span>
              </td>
            </tr>
            <tr>
              <td>
                <span className="contract-label">{t('crd:contracts:list:awardAmount')}</span>
                &nbsp;
                <span className="contract-value">
                  {award.getIn(['value', 'amount'], t('general:undefined'))}
                  &nbsp;
                  {award.getIn(['value', 'currency'])}
                </span>
              </td>
              <td>
                <span className="contract-label">
                  {t('crd:contracts:baseInfo:awardDate')}
                  {' '}
                </span>
                &nbsp;
                <span className="contract-value">
                  {award.has('date')
                    ? new Date(award.get('date')).toLocaleDateString()
                    : t('general:undefined')}
                </span>
              </td>
            </tr>
            <tr>
              <td>
                <span
                  className="contract-label"
                >
                  {t('crd:contracts:baseInfo:contractAmount')}
                </span>
                <span className="contract-value">
                  {data.get('contracts', []).length === 0 || data.get('contracts', [])
                    .toJS()[0].value === undefined ? t('general:undefined') : (`${data.get('contracts', [])
                      .toJS()[0].value.amount} ${data.get('contracts', []).toJS()[0].value.currency}`)}
                </span>
              </td>

              <td>
                <span className="contract-label">{t('crd:contracts:baseInfo:contractDateSigned')}</span>
                <span className="contract-value">
                  {data.get('contracts', []).length === 0 || data.get('contracts', [])
                    .toJS()[0].dateSigned === undefined ? t('general:undefined') : new Date(data.get('contracts', []).toJS()[0].dateSigned).toLocaleDateString()}
                </span>

              </td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

Info.propTypes = {
  t: PropTypes.func.isRequired,
};

class Contract extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = {
      contract: Map(),
      crosstab: Map(),
      indicators: {},
    };

    this.injectOcidFilter = cacheFn((filters, ocid) => ({
      ...filters,
      ocid: (filters.ocid || []).concat(ocid),
    }));
  }

  groupIndicators({ indicatorTypesMapping }, { contract }) {
    if (!indicatorTypesMapping || !Object.keys(indicatorTypesMapping).length || !contract) return;
    const newIndicators = {};
    contract.get('flags', List()).forEach((flag, name) => {
      if (flag.get && flag.get('value')) {
        indicatorTypesMapping[name].types.forEach((type) => {
          newIndicators[type] = newIndicators[type] || [];
          newIndicators[type].push(name);
        });
      }
    });
    this.setState({ indicators: newIndicators });
  }

  componentDidMount() {
    super.componentDidMount();
    this.groupIndicators(this.props, this.state);
  }

  componentWillUpdate(nextProps, nextState) {
    const indicatorsChanged = this.props.indicatorTypesMapping !== nextProps.indicatorTypesMapping;
    const contractChanged = this.state.contract !== nextState.contract;
    if (indicatorsChanged || contractChanged) {
      this.groupIndicators(nextProps, nextState);
    }
  }

  maybeGetFlagAnalysis() {
    const {
      filters, t, years, totalContracts, id,
    } = this.props;
    const { indicators, crosstab } = this.state;
    const noIndicators = Object.keys(indicators)
      .every((corruptionType) => !indicators[corruptionType] || !indicators[corruptionType].length);

    if (noIndicators) {
      return (
        <section className="flag-analysis">
          <h2>{t('crd:contracts:flagAnalysis')}</h2>
          <h4>{t('crd:contracts:noFlags')}</h4>
        </section>
      );
    }

    return (
      <section className="flag-analysis">
        <h2>
          {t('crd:contracts:flagAnalysis')}
          &nbsp;
          <small>
            (
            {t('crd:contracts:clickCrosstabHint')}
            )
          </small>
        </h2>
        {Object.keys(indicators).map((corruptionType) => {
          const nrFlags = indicators[corruptionType].length;
          return (
            <div key={corruptionType}>
              <h3>
                {t(`crd:corruptionType:${corruptionType}:pageTitle`)}
              </h3>
              <DataFetcher
                {...wireProps(this, [corruptionType, 'explanation'])}
                endpoint={`ocds/release/count?totalFlagged=${nrFlags}`}
              >
                <CrosstabExplanation
                  totalContracts={totalContracts}
                  nrFlags={nrFlags}
                  t={t}
                  corruptionType={corruptionType}
                />
              </DataFetcher>
              <Crosstab
                filters={this.injectOcidFilter(filters, id)}
                t={t}
                years={years}
                data={crosstab.get(corruptionType)}
                indicators={indicators[corruptionType]}
                requestNewData={(_, data) => {
                  const { crosstab } = this.state;
                  this.setState({ crosstab: crosstab.set(corruptionType, data) });
                }}
              />
            </div>
          );
        })}
      </section>
    );
  }

  render() {
    const { contract } = this.state;

    const {
      id, t, doSearch, filters, width, gotoSupplier, styling, isFeatureVisible,
    } = this.props;

    if (!contract) return null;

    const supplier = contract.get('awards', List())
      .find((award) => award.get('status') === 'active', undefined, Map())
      .getIn(['suppliers', 0]);

    const procuringEntityId = contract.getIn(['tender', 'procuringEntity', 'id'])
      || contract.getIn(['tender', 'procuringEntity', 'identifier', 'id']);

    const donutSize = width;

    return (
      <div className="contract-page">
        <TopSearch
          doSearch={doSearch}
          t={t}
          placeholder={t('crd:contracts:top-search')}
        />
        {isFeatureVisible('crd.contract.info')
        && (
          <Info
            id={id}
            data={contract}
            supplier={supplier}
            filters={filters}
            requestNewData={(_, contract) => this.setState({ contract })}
            t={t}
            gotoSupplier={gotoSupplier}
          />
        )}
        {isFeatureVisible('crd.contract.statistics')
        && (
          <section className="contract-statistics">
            <h2>
              {t('crd:contracts:contractStatistics')}
            </h2>
            {isFeatureVisible('crd.contract.statistics.nrOfBidders')
            && (
              <div className="col-sm-4">
                <CustomPopup
                  count={contract.getIn(['tender', 'tenderers'], List()).count()}
                  contract={contract}
                  {...wireProps(this, 'nrOfBidders')}
                  Popup={DonutPopup}
                  Chart={NrOfBidders}
                  layout={{
                    autosize: true,
                  }}
                  width={donutSize}
                  styling={styling}
                />
              </div>
            )}
            {isFeatureVisible('crd.contract.statistics.nrOfContractsWithThisPE')
            && (
              <div className="col-sm-4">
                {procuringEntityId && supplier
                && (
                  <CustomPopup
                    procuringEntityId={procuringEntityId}
                    supplierId={supplier.get('id')}
                    {...wireProps(this, 'nrContracts')}
                    Popup={DonutPopup}
                    Chart={NrOfContractsWithThisPE}
                    layout={{
                      autosize: true,
                    }}
                    width={donutSize}
                    styling={styling}
                  />
                )}
              </div>
            )}
            {isFeatureVisible('crd.contract.statistics.percentPESpending')
            && (
              <div className="col-sm-4">
                {procuringEntityId && supplier
                && (
                  <CustomPopup
                    procuringEntityId={procuringEntityId}
                    supplierId={supplier.get('id')}
                    {...wireProps(this, 'percentPESpending')}
                    Popup={PercentPESpendingPopup}
                    Chart={PercentPESpending}
                    layout={{
                      autosize: true,
                    }}
                    width={donutSize}
                    styling={styling}
                  />
                )}
              </div>
            )}
          </section>
        )}
        {isFeatureVisible('crd.contract.flagAnalysis')
        && isFeatureVisible('crd.contract.flagAnalysis.crosstab')
        && this.maybeGetFlagAnalysis()}
      </div>
    );
  }
}

Contract.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(Contract);
