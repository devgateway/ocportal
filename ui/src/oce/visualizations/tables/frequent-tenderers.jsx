import { List } from 'immutable';
import URI from 'urijs';
import Table from './index';
import orgNamesFetching from '../../orgnames-fetching';
import { send, callFunc } from '../../tools';
import fmConnect from '../../fm/fm';

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
    const newData1 = [];
    Promise.all(
      data.map((datum) => send(new URI('/api/activeAwardsCount').addSearch('bidderId',
        [datum.get('tendererId1'), datum.get('tendererId2')])
        .addSearch('supplierId', datum.get('tendererId1'))).then(callFunc('json')).then((r) => newData1.push(datum.set('supplierCount1', r[0] === undefined ? 0 : r[0].cnt)))),
    )
      .then(
        () => {
          const newData = [];
          Promise.all(
            newData1.map((datum) => send(new URI('/api/activeAwardsCount').addSearch('bidderId',
              [datum.get('tendererId1'), datum.get('tendererId2')])
              .addSearch('supplierId', datum.get('tendererId1'))).then(callFunc('json')).then((r) => newData.push(datum.set('supplierCount2', r[0] === undefined ? 0 : r[0].cnt)))),
          )
            .then(
              () => {
                this.setState({ newData });
              },
            );
        },
      );
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
    return (
      <table className="table table-stripped trable-hover frequent-supplier-bidder-table">
        <thead>
          <tr>
            <th>
              {this.t('tables:frequentTenderers:supplier')}
              {' '}
              #1
            </th>
            <th>
              {this.t('tables:frequentTenderers:supplier')}
              {' '}
              #2
            </th>
            <th>{this.t('tables:frequentTenderers:nrITB')}</th>
            <th>{this.t('tables:frequentTenderers:supplier1wins')}</th>
            <th>{this.t('tables:frequentTenderers:supplier2wins')}</th>
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
                {this.t('tables:showAll')}
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

export default fmConnect(FrequentTenderers, 'viz.me.table.frequentTenderers');
