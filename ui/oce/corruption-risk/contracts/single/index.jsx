import { Map, List } from 'immutable';
import CRDPage from '../../page';
import Visualization from '../../../visualization';
import translatable from '../../../translatable';
import styles from '../style.less';
import TopSearch from '../top-search';
import NrOfBidders from './donuts/nr-of-bidders';
import NrOfContractsWithThisPE from './donuts/nr-contract-with-pe';
import PercentPESpending from './donuts/percent-pe-spending';
import Crosstab from '../../clickable-crosstab';
import { CORRUPTION_TYPES } from '../../constants';

class Info extends translatable(Visualization) {
  constructor(...args){
    super(...args);
    this.state.showAllSuppliers = false;
  }

  getCustomEP(){
    const { id } = this.props;
    return `flaggedRelease/ocid/${id}`;
  }

  getSuppliers(){
    const { data } = this.props;
    const { showAllSuppliers } = this.state;
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    return showAllSuppliers ?
      suppliers :
      suppliers.slice(0, 2);
  }

  render() {
    const { data } = this.props;

    const title = data.getIn(['tender', 'title']);
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    const startDate = data.getIn(['tender', 'tenderPeriod', 'startDate']);
    const endDate = data.getIn(['tender', 'tenderPeriod', 'endDate']);
    const award = data.get('awards', List()).find(award =>
      award.get('status') != 'unsuccessful') || Map();

    return (
      <section>
        <dl>
          <dt>{this.t('crd:procurementsTable:contractID')}</dt>
          <dd>{data.get('ocid')}</dd>
          {title && <dt>{this.t('crd:general:contract:title')}</dt>}
          {title && <dd>{title}</dd>}
        </dl>
        <table className="table table-bordered join-bottom info-table">
          <tbody>
            <tr>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:procuringEntityName')}</dt>
                  <dd>{data.getIn(['tender', 'procuringEntity', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:buyer')}</dt>
                  <dd>{data.getIn(['buyer', 'name'], this.t('general:undefined'))}</dd>
                </dl>
              </td>
              <td>
                <dl>
                  <dt>{this.t('crd:contracts:baseInfo:suppliers')}</dt>
                  <dd>
                    {suppliers.count() ?
                      this.getSuppliers().map(supplier => <p>{supplier.get('name')}</p>) :
                      this.t('general:undefined')
                    }
                  </dd>
                  <a href="javascript:void(0);" onClick={() => this.setState({showAllSuppliers: true})}></a>
                </dl>
              </td>
            </tr>
          </tbody>
        </table>
        <table className="table table-bordered">
          <tbody>
            <tr>
              <td>
                {this.t('crd:contracts:baseInfo:tenderAmount')}
                &nbsp;
                <strong>
                  {data.getIn(['tender', 'value', 'amount'], this.t('general:undefined'))}
                  &nbsp;
                  {data.getIn(['tender', 'value', 'currency'])}
                </strong>
              </td>
              <td>
                {this.t('crd:contracts:baseInfo:tenderDates')}
                &nbsp;
                <strong>
                  {startDate ?
                    <span>
                      {new Date(startDate).toLocaleDateString()}
                      &ndash;
                    </span> :
                    this.t('general:undefined')}

                  {endDate && new Date(endDate).toLocaleDateString()}
                </strong>
              </td>
            </tr>
            <tr>
              <td>
                {this.t('crd:contracts:list:awardAmount')}
                &nbsp;
                <strong>
                  {award.getIn(['value', 'amount'], this.t('general:undefined'))}
                  &nbsp;
                  {award.getIn(['value', 'currency'])}
                </strong>
              </td>
              <td>
                {this.t('crd:contracts:baseInfo:awardDate')}
                &nbsp;
                <strong>
                  {award.has('date') ?
                    new Date(award.get('date')).toLocaleDateString() :
                    this.t('general:undefined')}
                </strong>
              </td>
            </tr>
            <tr>
              <td>{this.t('crd:contracts:baseInfo:contractAmount')}</td>
              <td>{this.t('crd:contracts:baseInfo:contractDates')}</td>
            </tr>
          </tbody>
        </table>
      </section>
    );
  }
}

export default class Contract extends CRDPage {
  constructor(...args){
    super(...args);
    this.state = {
      contract: Map(),
      crosstab: Map(),
      indicators: {}
    }
  }

  getSuppliers(){
    const { data } = this.props;
    const { showAllSuppliers } = this.state;
    const suppliers = data.get('awards', List()).flatMap(award => award.get('suppliers'));
    return showAllSuppliers ?
      suppliers :
      suppliers.slice(0, 2);
  }

  groupIndicators({ indicatorTypesMapping }, { contract }) {
    if (!indicatorTypesMapping || !Object.keys(indicatorTypesMapping).length || !contract) return;
    const newIndicators = {};
    contract.get('flags', List()) .forEach((flag, name) => {
      if(flag.get && flag.get('value')) {
        indicatorTypesMapping[name].types.forEach(type => {
          newIndicators[type] = newIndicators[type] || [];
          newIndicators[type].push(name);
        })
      }
    });
    this.setState({ indicators: newIndicators });
  }

  componentDidMount() {
    super.componentDidMount();
    this.groupIndicators(this.props, this.state);
  }

  componentWillUpdate(nextProps, nextState) {
    const indicatorsChanged = this.props.indicatorTypesMapping != nextProps.indicatorTypesMapping;
    const contractChanged = this.state.contract != nextState.contract;
    if (indicatorsChanged || contractChanged) {
      this.groupIndicators(nextProps, nextState);
    }
  }

  maybeGetFlagAnalysis() {
    const { filters, translations, years } = this.props;
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
        {Object.keys(indicators).map(corruptionType => (
          <div>
            <h3>
              {this.t(`crd:corruptionType:${corruptionType}:pageTitle`)}
            </h3>
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
        ))}
      </section>
    );
  }

  render() {
    const { contract, nrOfBidders, nrContracts, percentPESpending, crosstab,
      indicators } = this.state;

    const { id, translations, doSearch, indicatorTypesMapping, filters, years } = this.props;
    return (
      <div className="contract-page">
        <TopSearch
          doSearch={doSearch}
          translations={translations}
        />
        <Info
          id={id}
          data={contract}
          filters={Map()}
          requestNewData={(_, contract) => this.setState({contract})}
          translations={translations}
        />
        <section>
          <h2>
            {contract.get('flags', List()).filter(flag => flag.get && flag.get('value')).count()}
            &nbsp;
            Flags
          </h2>
          <div className="col-md-4">
            <NrOfBidders
              contract={contract}
              data={nrOfBidders}
              filters={Map()}
              years={Map()}
              requestNewData={(_, nrOfBidders) => this.setState({ nrOfBidders })}
              translations={translations}
            />
          </div>
          <div className="col-md-4">
            <NrOfContractsWithThisPE
              contract={contract}
              data={nrContracts}
              filters={Map()}
              years={Map()}
              requestNewData={(_, nrContracts) => this.setState({ nrContracts })}
              translations={translations}
            />
          </div>
          <div className="col-md-4">
            <PercentPESpending
              data={percentPESpending}
              filters={Map()}
              years={Map()}
              requestNewData={(_, percentPESpending) => this.setState({ percentPESpending })}
              translations={translations}
            />
          </div>
        </section>
        {this.maybeGetFlagAnalysis()}
      </div>
    );
  }
}
