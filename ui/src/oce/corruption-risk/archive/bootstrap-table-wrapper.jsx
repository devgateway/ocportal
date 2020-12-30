import React from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import paginationFactory, {
  PaginationProvider,
  SizePerPageDropdownStandalone,
  PaginationListStandalone,
} from 'react-bootstrap-table2-paginator';
import PropTypes from 'prop-types';
import './styles.scss';
import fmConnect from '../../fm/fm';

/* eslint-disable react/jsx-props-no-spreading */

const PaginationRow = (paginationProps) => (
  <div className="row react-bootstrap-table-pagination">
    <div className="col-md-6 col-xs-6 col-sm-6 col-lg-6">
      <SizePerPageDropdownStandalone {...paginationProps} />
    </div>
    <div className="react-bootstrap-table-pagination-list col-md-6 col-xs-6 col-sm-6 col-lg-6">
      <PaginationListStandalone {...paginationProps} />
    </div>
  </div>
);

const BootstrapTableWrapper = ({
  columns,
  data,
  page,
  pageSize,
  onPageChange,
  onSizePerPageList,
  count,
  containerClass,
  bordered,
  isFeatureVisible,
}) => {
  const visibleColumns = columns.filter((column) => !column.fm || isFeatureVisible(column.fm));
  return (
    <PaginationProvider
      pagination={paginationFactory({
        custom: true,
        withFirstAndLast: true,
        page,
        sizePerPage: pageSize,
        totalSize: count,
        sizePerPageList: [20, 50, 100, 200].map((value) => ({ text: value, value })),
      })}
    >
      {
        ({ paginationProps, paginationTableProps }) => (
          <div className="react-bs-table-container">
            <PaginationRow {...paginationProps} />

            <BootstrapTable
              data={data}
              striped
              bordered={bordered}
              remote
              wrapperClasses={containerClass}
              keyField="id"
              columns={visibleColumns}
              onTableChange={(type, { page: newPage, sizePerPage }) => {
                onPageChange(newPage);
                onSizePerPageList(sizePerPage);
              }}
              {...paginationTableProps}
            />

            <PaginationRow {...paginationProps} />
          </div>
        )
      }
    </PaginationProvider>
  );
};

BootstrapTableWrapper.defaultProps = {
  bordered: true,
  containerClass: null,
  page: 1,
  pageSize: 20,
};

BootstrapTableWrapper.propTypes = {
  bordered: PropTypes.bool,
  containerClass: PropTypes.string,
  page: PropTypes.number,
  count: PropTypes.number.isRequired,
  data: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
  })).isRequired,
  pageSize: PropTypes.number,
  onPageChange: PropTypes.func.isRequired,
  onSizePerPageList: PropTypes.func.isRequired,
  columns: PropTypes.arrayOf(PropTypes.shape({
    dataField: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired,
    fm: PropTypes.string,
    formatter: PropTypes.func,
    style: PropTypes.objectOf(PropTypes.string),
  })).isRequired,
  isFeatureVisible: PropTypes.func.isRequired,
};

export default fmConnect(BootstrapTableWrapper);
