import { Map, List, Set } from 'immutable';
import CRDPage from '../../page';
import Visualization from '../../../visualization';
import translatable from '../../../translatable';
import TopSearch from '../../top-search';
import NrOfBidders from './donuts/nr-of-bidders';
import NrOfContractsWithThisPE from './donuts/nr-contract-with-pe';
import PercentPESpending from './donuts/percent-pe-spending';
import PercentPESpendingPopup from './donuts/percent-pe-spending/popup';
import Crosstab from '../../clickable-crosstab';
import CustomPopup from '../../custom-popup';
import DonutPopup from '../../donut/popup';
import { wireProps } from '../../tools';
// eslint-disable-next-line no-unused-vars
import styles from '../style.less';
import DataFetcher from '../../data-fetcher';

class CrosstabExplanation extends translatable(React.PureComponent) {
  render() {
    const { data, totalContracts, nrFlags, corruptionType } = this.props;
    const template = nrFlags === 1 ?
      this.t('crd:contracts:crosstab:explanation:sg') :
      this.t('crd:contracts:crosstab:explanation:pl');

    return (
      <p>
        {template.replace('$#$', data)
          .replace('$#$', (data / totalContracts * 100).toFixed(2))
          .replace('$#$', nrFlags)
          .replace(
            '$#$',
            this.t(`crd:corruptionType:${corruptionType}:name`)
                .toLowerCase()
          )}
      </p>
    );
  }
};

class Info extends translatable(Visualization) {
  getCustomEP() {
    const { id } = this.props;
    return `flaggedRelease/ocid/${id}`;
  }

  render() {
    const { data, supplier, gotoSupplier } = this.props;

    const title = data.getIn(['tender', 'title']);
    const startDate = data.getIn(['tender', 'tenderPeriod', 'startDate']);
    const endDate = data.getIn(['tender', 'tenderPeriod', 'endDate']);
    const award = data.get('awards', List()).find(a =>
      a.get('status') === 'active') || Map();

    const PE = data.getIn(['tender', 'procuringEntity']);

    const flagCount = data.get('flags', List()).filter(flag => flag.get && flag.get('value')).count();
    return (
      <section className="info">
        <div className="row">
          <dl className="col-md-4">
            <dt><span className="contract-label">{this.t('crd:procurementsTable:contractID')}</span></dt>
            <dd><span className="contract-value">{data.get('ocid')}</span></dd>
          </dl>
          <dl className="col-md-4">
            <dt><span className="contract-label">{this.t('crd:contracts:baseInfo:status')}</span></dt>
            <dd><span className="contract-value">{data.get('tag', []).join(', ')}</span></dd>
          </dl>
          <div className="col-md-4 flags">
            <img src="assets/icons/flag.svg" alt="Flag icon" className="flag-icon" />
            &nbsp;
            <span className="count">
              {flagCount}
              &nbsp;
              {this.t(flagCount === 1 ?
                'crd:contracts:baseInfo:flag:sg' :
                'crd:contracts:baseInfo:flag:pl')}
            </span>
          </div>
        </div>
        {title &&
          <dl>
            {title && <dt>{this.t('crd:general:contract:title')}</dt>}
            {title && <dd>{title}</dd>}
          </dl>
        }
        <table className="table table-bordered join-bottom info-table">
          <tbody>
            <tr>
              <td>
                {PE && <dl>
                  <dt><span className="contract-label">{this.t('crd:contracts:baseInfo:procuringEntityName')}</span></dt>
                  <dd>
                    <a
                      href={`#!/crd/procuring-entity/${PE.get('id')}`}
                    >
                      {PE.get('name')}
                    </a>
                  </dd>
                </dl>}
              </td>
              <td>
                <dl>
                  <dt><span className="contract-label">{this.t('crd:contracts:baseInfo:buyer')}</span></dt>
                  <dd><span className="contract-value">{data.getIn(['buyer', 'name'], this.t('general:undefined'))}</span></dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt><span className="contract-label">{this.t('crd:contracts:baseInfo:suppliers')}</span></dt>
                  <dd>
                    <span className="contract-value">{supplier ?
                      <a
                        href={`#!/crd/supplier/${supplier.get('id')}`}
                        onClick={gotoSupplier}
                      >
                        {supplier.get('name')}
                      </a> :
                      this.t('general:undefined')
                    }
                    </span>
                  </dd>
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered">
          <tbody>
            <tr>
              <td>
                <span className="contract-label">{this.t('crd:contracts:baseInfo:tenderAmount')}:</span>
                &nbsp;
                <span className="contract-value">
                  {data.getIn(['tender', 'value', 'amount'], this.t('general:undefined'))}
                  &nbsp;
                  {data.getIn(['tender', 'value', 'currency'])}
                </span>
              </td>
              <td>
                <span className="contract-label">{this.t('crd:contracts:baseInfo:tenderDates')}:</span>
                &nbsp;
                <span className="contract-value">
                  {startDate ?
                    <span>
                      {new Date(startDate).toLocaleDateString()}
                      &ndash;
                    </span> :
                    this.t('general:undefined')}

                  {endDate && new Date(endDate).toLocaleDateString()}
                </span>
              </td>
            </tr>
            <tr>
              <td>
                <span className="contract-label">{this.t('crd:contracts:list:awardAmount')}:</span>
                &nbsp;
                <span className="contract-value">
                  {award.getIn(['value', 'amount'], this.t('general:undefined'))}
                  &nbsp;
                  {award.getIn(['value', 'currency'])}
                </span>
              </td>
              <td>
                <span className="contract-label">{this.t('crd:contracts:baseInfo:awardDate')}: </span>
                &nbsp;
                <span className="contract-value">
                  {award.has('date') ?
                    new Date(award.get('date')).toLocaleDateString() :
                    this.t('general:undefined')}
                </span>
              </td>
            </tr>
            <tr>
              <td><span className="contract-label">{this.t('crd:contracts:baseInfo:contractAmount')}</span></td>
              <td><span className="contract-label">{this.t('crd:contracts:baseInfo:contractDates')}</span></td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

export default class Contract extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = {
      contract: Map(),
      crosstab: Map(),
      indicators: {},
    };
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
    const { filters, translations, years, totalContracts } = this.props;
    const { indicators, crosstab } = this.state;
    const noIndicators = Object.keys(indicators)
      .every(corruptionType =>
        !indicators[corruptionType] || !indicators[corruptionType].length);

    if (noIndicators) return (
      <section className="flag-analysis">
        <h2>{this.t('crd:contracts:flagAnalysis')}</h2>
        <h4>{this.t('crd:contracts:noFlags')}</h4>
      </section>
    );

    return (
      <section className="flag-analysis">
        <h2>
          {this.t('crd:contracts:flagAnalysis')}
          &nbsp;
          <small>({this.t('crd:contracts:clickCrosstabHint')})</small>
        </h2>
        {Object.keys(indicators).map(corruptionType => {
           const nrFlags = indicators[corruptionType].length;
           return (
             <div>
               <h3>
                 {this.t(`crd:corruptionType:${corruptionType}:pageTitle`)}
               </h3>
               <DataFetcher
                 {...wireProps(this, [corruptionType, 'explanation'])}
                 endpoint={`ocds/release/count?totalFlagged=${nrFlags}`}
               >
                 <CrosstabExplanation
                   totalContracts={totalContracts}
                   nrFlags={nrFlags}
                   translations={translations}
                   corruptionType={corruptionType}
                 />
               </DataFetcher>
               <Crosstab
                 filters={filters}
                 translations={translations}
                 years={years}
                 data={crosstab.get(corruptionType)}
                 indicators={indicators[corruptionType]}
                 requestNewData={(_, data) => {
                     const { crosstab } = this.state;
                     this.setState({ crosstab: crosstab.set(corruptionType, data)})
                 }}
               />
             </div>
           )})}
      </section>
    );
  }

  render() {
    const { contract } = this.state;

    const { id, translations, doSearch, filters, width, gotoSupplier } = this.props;

    if (!contract) return null;

    const supplier = contract.get('awards', List())
      .find(award => award.get('status') === 'active', undefined, Map())
      .getIn(['suppliers', 0]);

    const procuringEntityId = contract.getIn(['tender', 'procuringEntity', 'id']) ||
      contract.getIn(['tender', 'procuringEntity', 'identifier', 'id']);

    const donutSize = width / 3 - 100;

    return (
      <div className="contract-page">
        <TopSearch
          doSearch={doSearch}
          translations={translations}
          placeholder={this.t('crd:contracts:top-search')}
        />
        <Info
          id={id}
          data={contract}
          supplier={supplier}
          filters={filters}
          requestNewData={(_, contract) => this.setState({ contract })}
          translations={translations}
          gotoSupplier={gotoSupplier}
        />

        <section className="contract-statistics">
          <h2>
            {this.t('crd:contracts:contractStatistics')}
          </h2>
          <div className="col-sm-4">
            <CustomPopup
              count={contract.getIn(['tender', 'tenderers'], List()).count()}
              contract={contract}
              {...wireProps(this, 'nrOfBidders')}
              Popup={DonutPopup}
              Chart={NrOfBidders}
              width={donutSize}
            />
          </div>
          <div className="col-sm-4">
            {procuringEntityId && supplier &&
              <CustomPopup
                procuringEntityId={procuringEntityId}
                supplierId={supplier.get('id')}
                {...wireProps(this, 'nrContracts')}
                Popup={DonutPopup}
                Chart={NrOfContractsWithThisPE}
                width={donutSize}
              />
            }
          </div>
          <div className="col-sm-4">
            {procuringEntityId && supplier &&
              <CustomPopup
                procuringEntityId={procuringEntityId}
                supplierId={supplier.get('id')}
                {...wireProps(this, 'percentPESpending')}
                Popup={PercentPESpendingPopup}
                Chart={PercentPESpending}
                width={donutSize}
              />
            }
          </div>
        </section>
        {this.maybeGetFlagAnalysis()}
      </div>
    );
  }
}
