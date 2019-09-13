import { List } from 'immutable';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';
import { BuyersIds, BuyersTendersCount, BuyersAwardsCount } from './state';

const mkLink = navigate => (content, { id }) => (
  <a
    href={`#!/crd/buyer/${id}`}
    onClick={() => navigate('buyer', id)}
  >
    {content}
  </a>
);

class BuyerList extends PaginatedTable {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.tenders = {};
    this.state.awards = {};
  }

  getCustomEP() {
    const { searchQuery } = this.props;
    const eps = super.getCustomEP();
    return searchQuery ?
      eps.map(ep => ep.addSearch('text', decodeURIComponent(searchQuery))) :
      eps;
  }

  componentWillMount() {
    BuyersTendersCount.addListener(
      'BuyerList',
      this.updateBindings.bind(this),
    );
    BuyersAwardsCount.addListener(
      'BuyerList',
      this.updateBindings.bind(this),
    );
  }

  updateBindings() {
    Promise.all([
      BuyersTendersCount.getState(),
      BuyersAwardsCount.getState(),
    ]).then(([tenders, awards]) => {
      this.setState({ tenders, awards });
    });
  }

  componentWillUnmount() {
    BuyersTendersCount.removeListener('BuyerList');
    BuyersAwardsCount.removeListener('BuyerList');
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
    if (propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }

    const { data } = this.props;
    if (prevProps.data !== data) {
      BuyersIds.assign(
        'BuyersIds',
        this.props.data
          .get('data', List())
          .map(datum => datum.get('buyerId'))
      );
    }
  }

  render() {
    const { data, navigate } = this.props;

    const count = data.get('count', 0);

    const { pageSize, page, tenders, awards } = this.state;

    const jsData = data.get('data', List()).map((supplier) => {
      const id = supplier.get('buyerId');
      return {
        id,
        name: supplier.get('buyerName'),
        nrTenders: tenders[id],
        nrAwards: awards[id],
        nrFlags: supplier.get('countFlags'),
      }
    }).toJS();

    return (
      <BootstrapTable
        data={jsData}
        striped
        pagination
        remote
        fetchInfo={{
          dataTotalSize: count,
        }}
        options={{
          page,
          onPageChange: newPage => this.setState({ page: newPage }),
          sizePerPage: pageSize,
          sizePerPageList: [20, 50, 100, 200].map(value => ({ text: value, value })),
          onSizePerPageList: newPageSize => this.setState({ pageSize: newPageSize }),
          paginationPosition: 'both',
        }}
      >
        <TableHeaderColumn dataField='id' isKey dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:ID')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='name' dataFormat={mkLink(navigate)}>
          {this.t('crd:suppliers:name')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrTenders'>
          {this.t('crd:procuringEntities:noOfTenders')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrAwards'>
          {this.t('crd:procuringEntities:noOfAwards')}
        </TableHeaderColumn>
        <TableHeaderColumn dataField='nrFlags'>
          {this.t('crd:procurementsTable:noOfFlags')}
        </TableHeaderColumn>
      </BootstrapTable>
    );
  }
}

class Buyers extends CRDPage {
  requestNewData(path, newData) {
    this.props.requestNewData(
      path,
      newData.set('count', newData.getIn(['count', 0, 'count'], 0))
    );
  }

  render() {
    const { navigate, searchQuery, doSearch } = this.props;
    return (
      <Archive
        {...wireProps(this)}
        requestNewData={this.requestNewData.bind(this)}
        searchQuery={searchQuery}
        doSearch={doSearch}
        navigate={navigate}
        className="procuring-entities-page"
        topSearchPlaceholder={this.t('crd:buyers:top-search')}
        List={BuyerList}
        dataEP="buyersByFlags"
        countEP="buyersByFlags/count"
      />
    );
  }
}

export default Buyers;
