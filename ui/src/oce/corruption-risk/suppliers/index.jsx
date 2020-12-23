import React from 'react';
import URI from 'urijs';
import { List, Map } from 'immutable';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';
import { fetchEP, pluckImm, cacheFn } from '../../tools';
import BackendDateFilterable from '../backend-date-filterable';
import BootstrapTableWrapper from '../archive/bootstrap-table-wrapper';

export const mkLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/supplier/${id}`}
    onClick={() => navigate('supplier', id)}
  >
    {content}
  </a>
);

class SList extends PaginatedTable {
  getCustomEP() {
    const { searchQuery } = this.props;
    const eps = super.getCustomEP();
    return searchQuery ?
      eps.map(ep => ep.addSearch('text',  decodeURIComponent(searchQuery))) :
      eps;
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
    if (propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }
  }

  render() {
    const { data, navigate } = this.props;

    const count = data.get('count', 0);

    const { pageSize, page } = this.state;

    const jsData = data.get('data', List()).map((supplier) => {
      return {
        id: supplier.get('supplierId'),
        name: supplier.get('supplierName'),
        wins: supplier.get('wins'),
        winAmount: supplier.get('winAmount'),
        losses: supplier.get('losses'),
        flags: supplier.get('countFlags'),
      }
    }).toJS();

    return (
      <BootstrapTableWrapper
        data={jsData}
        count={count}
        page={page}
        onPageChange={(newPage) => this.setState({ page: newPage })}
        pageSize={pageSize}
        onSizePerPageList={(newPageSize) => this.setState({ pageSize: newPageSize })}
        columns={[
          {
            text: this.t('crd:suppliers:name'),
            dataField: 'name',
            formatter: mkLink(navigate),
          },
          {
            text: this.t('crd:suppliers:ID'),
            dataField: 'id',
            formatter: mkLink(navigate),
          },
          {
            text: this.t('crd:suppliers:wins'),
            dataField: 'wins',
          },
          {
            text: this.t('crd:suppliers:losses'),
            dataField: 'losses',
          },
          {
            text: this.t('crd:suppliers:totalWon'),
            dataField: 'winAmount',
          },
          {
            text: this.t('crd:suppliers:nrFlags'),
            dataField: 'flags',
          },
        ]}
      />
    );
  }
}

class Suppliers extends CRDPage {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.winLossFlagInfo = Map();
    this.injectWinLossData = cacheFn((data, winLossFlagInfo) => {
      return data.update('data', List(), list => list.map(supplier => {
        const id = supplier.get('supplierId');
        if (!winLossFlagInfo.has(id)) return supplier;
        const info = winLossFlagInfo.get(id);
        return supplier
          .set('wins', info.won.count)
          .set('winAmount', info.won.totalAmount)
          .set('losses', info.lostCount)
          .set('flags', info.applied.countFlags)
      }))
    });
  }

  onNewDataRequested(path, newData) {
    const supplierIds = newData.get('data').map(pluckImm('supplierId'));
    this.setState({ winLossFlagInfo: Map() });
    if (supplierIds.toJS().length !== 0) {
      fetchEP(new URI('/api/procurementsWonLost').addSearch({
        bidderId: supplierIds.toJS(),
      }))
        .then(result => {
          this.setState({
            winLossFlagInfo: Map(result.map(datum => [
              datum.applied._id,
              datum
            ]))
          });
        });
    }
    this.props.requestNewData(
      path,
      newData.set('count', newData.getIn(['count', 0, 'count'], 0))
    );
  }

  render() {
    const { navigate, searchQuery, doSearch, data } = this.props;
    const { winLossFlagInfo } = this.state;
    return (
      <BackendDateFilterable
        {...wireProps(this)}
        data={this.injectWinLossData(data, winLossFlagInfo)}
        requestNewData={this.onNewDataRequested.bind(this)}
      >
        <Archive
          searchQuery={searchQuery}
          doSearch={doSearch}
          navigate={navigate}
          className="suppliers-page"
          topSearchPlaceholder={this.t('crd:suppliers:top-search')}
          List={SList}
          dataEP="suppliersByFlags"
          countEP="suppliersByFlags/count"
        />
      </BackendDateFilterable>
    );
  }
}

export default Suppliers;
