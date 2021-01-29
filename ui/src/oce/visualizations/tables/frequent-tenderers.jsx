import { List } from 'immutable';
import Table from './index';
import orgNamesFetching from '../../orgnames-fetching';
import { fetchEP } from '../../tools';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

const maybeSlice = (flag, list) => (flag ? list.slice(0, 10) : list);

class FrequentTenderers extends orgNamesFetching(Table) {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.showAll = false;
    this.state.newData = [];
  }

  row(entry, index) {
    const id1 = entry.get('tendererId1');
    const id2 = entry.get('tendererId2');
    return (
      <tr key={index}>
        <td>{this.getOrgName(id1)}</td>
        <td>{this.getOrgName(id2)}</td>
        <td>{entry.get('pairCount')}</td>
        <td>{entry.get('supplierCount1')}</td>
        <td>{entry.get('supplierCount2')}</td>
      </tr>
    );
  }

  getOrgsWithoutNamesIds() {
    const { data } = this.props;
    if (!data) return [];
    return data.map((datum) => List([datum.get('tendererId1'), datum.get('tendererId2')]))
      .flatten()
      .filter((id) => !this.state.orgNames[id])
      .toJS();
  }

  maybeGetSupplierWins() {
    const { data } = this.props;
    if (!data) return;

    const url = this.buildUrl('activeAwardsCount')
      .addSearch('leftBidderIds', data.map((datum) => datum.get('tendererId1')).toJS().join(','))
      .addSearch('rightBidderIds', data.map((datum) => datum.get('tendererId2')).toJS().join(','));
    fetchEP(url)
      .then((allCounts) => {
        const newData = data.map((datum, key) => datum
          .set('supplierCount1', allCounts[key][0])
          .set('supplierCount2', allCounts[key][1]));
        this.setState({ newData });
      });
  }

  componentDidMount() {
    super.componentDidMount();
    this.maybeGetSupplierWins();
  }

  componentDidUpdate(prevProps, ...args) {
    super.componentDidUpdate(prevProps, ...args);
    if (prevProps.data !== this.props.data) {
      this.maybeGetSupplierWins();
    }
  }

  render() {
    if (!this.props.data) return null;
    const { showAll, newData } = this.state;
    const { t } = this.props;
    return (
      <table className="table table-stripped trable-hover frequent-supplier-bidder-table">
        <thead>
          <tr>
            <th>
              {t('tables:frequentTenderers:supplier')}
              {' '}
              #1
            </th>
            <th>
              {t('tables:frequentTenderers:supplier')}
              {' '}
              #2
            </th>
            <th>{t('tables:frequentTenderers:nrITB')}</th>
            <th>{t('tables:frequentTenderers:supplier1wins')}</th>
            <th>{t('tables:frequentTenderers:supplier2wins')}</th>
          </tr>
        </thead>
        <tbody>
          {maybeSlice(!showAll, newData)
            .map((entry, index) => this.row(entry, index))}
          {!showAll && this.props.data.count() > 10 && (
          <tr>
            <td colSpan="5">
              <button
                className="btn btn-info btn-danger btn-block"
                onClick={() => this.setState({ showAll: true })}
              >
                {t('tables:showAll')}
              </button>
            </td>
          </tr>
          )}
        </tbody>
      </table>
    );
  }
}

FrequentTenderers.getName = (t) => t('tables:frequentTenderers:title');
FrequentTenderers.endpoint = 'frequentTenderers';

FrequentTenderers.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(FrequentTenderers, 'viz.me.table.frequentTenderers');
