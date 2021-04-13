import React from 'react';
import { List } from 'immutable';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import CRDPage from '../page';
import PaginatedTable from '../paginated-table';
import Archive from '../archive';
import { wireProps } from '../tools';
import { getTenderAndAwardCounts } from './api';
import BootstrapTableWrapper from '../archive/bootstrap-table-wrapper';

export const mkLink = (content, { id }) => (
  <Link
    to={`/portal/crd/procuring-entity/${id}`}
  >
    {content}
  </Link>
);

class PEList extends PaginatedTable {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.tenders = {};
    this.state.awards = {};
  }

  getCustomEP() {
    const { searchQuery } = this.props;
    const eps = super.getCustomEP();
    return searchQuery
      ? eps.map((ep) => ep.addSearch('text', decodeURIComponent(searchQuery)))
      : eps;
  }

  componentDidUpdate(prevProps, prevState) {
    const propsChanged = ['filters', 'searchQuery'].some((key) => this.props[key] !== prevProps[key]);
    if (propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }

    const { data } = this.props;
    if (prevProps.data !== data) {
      this.fetchCounts();
    }
  }

  fetchCounts() {
    const {
      filters, years, months, data,
    } = this.props;

    const ids = data
      .get('data', List())
      .map((datum) => datum.get('procuringEntityId'))
      .toArray();

    const peFilters = {
      ...filters,
      year: years,
      month: months,
      procuringEntityId: ids,
    };

    if (peFilters.procuringEntityId.length > 0) {
      getTenderAndAwardCounts(peFilters)
        .then(
          ([tenders, awards]) => this.setState({ tenders, awards }),
          () => this.setState({ tenders: {}, awards: {} }),
        );
    }
  }

  render() {
    const { data, t } = this.props;

    const count = data.get('count', 0);

    const {
      pageSize, page, tenders, awards,
    } = this.state;

    const jsData = data.get('data', List()).map((supplier) => {
      const id = supplier.get('procuringEntityId');
      return {
        id,
        name: supplier.get('procuringEntityName'),
        nrTenders: tenders[id],
        nrAwards: awards[id],
        nrFlags: supplier.get('countFlags'),
      };
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
            text: t('crd:suppliers:ID'),
            dataField: 'id',
            fm: 'crd.procuringEntities.col.id',
            formatter: mkLink,
          },
          {
            text: t('crd:suppliers:name'),
            dataField: 'name',
            fm: 'crd.procuringEntities.col.name',
            formatter: mkLink,
          },
          {
            text: t('crd:procuringEntities:noOfTenders'),
            dataField: 'nrTenders',
            fm: 'crd.procuringEntities.col.nrTenders',
          },
          {
            text: t('crd:procuringEntities:noOfAwards'),
            dataField: 'nrAwards',
            fm: 'crd.procuringEntities.col.nrAwards',
          },
          {
            text: t('crd:procurementsTable:noOfFlags'),
            dataField: 'nrFlags',
            fm: 'crd.procuringEntities.col.nrFlags',
          },
        ]}
      />
    );
  }
}

PEList.propTypes = {
  t: PropTypes.func.isRequired,
};

class ProcuringEntities extends CRDPage {
  requestNewData(path, newData) {
    this.props.requestNewData(
      path,
      newData.set('count', newData.getIn(['count', 0, 'count'], 0)),
    );
  }

  render() {
    const { searchQuery, doSearch, t } = this.props;
    return (
      <Archive
        {...wireProps(this)}
        requestNewData={this.requestNewData.bind(this)}
        searchQuery={searchQuery}
        doSearch={doSearch}
        className="procuring-entities-page"
        topSearchPlaceholder={t('crd:procuringEntities:top-search')}
        List={PEList}
        dataEP="procuringEntitiesByFlags"
        countEP="procuringEntitiesByFlags/count"
      />
    );
  }
}

ProcuringEntities.propTypes = {
  t: PropTypes.func.isRequired,
};

export default ProcuringEntities;
