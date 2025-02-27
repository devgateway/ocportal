import React from 'react';
import DataTable from 'react-data-table-component';
import PropTypes from 'prop-types';
import './styles.scss';
import fmConnect from '../../fm/fm';

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
  const transformedColumns = visibleColumns.map((column) => ({
    name: column.text,
    selector: (row) => row[column.dataField], // This extracts the correct field from row data
    cell: column.formatter ? (row) => column.formatter(row[column.dataField], row) : null, // Apply formatter if provided
    sortable: true, // You can make columns sortable, if needed
    className: column.classes, // Apply custom column class
    headerClassName: column.headerClasses,
    style: column.style, // Apply any custom styles
  }));
  return (
    <div className={`react-data-table-container ${containerClass || ''}`}>
      <DataTable
        columns={transformedColumns}
        data={data}
        pagination
        paginationServer
        paginationTotalRows={count}
        paginationDefaultPage={page}
        paginationPerPage={pageSize}
        paginationComponentOptions={{
          rowsPerPageText: 'Rows per page:',
          rangeSeparatorText: 'of',
          selectAllRowsItem: true,
          selectAllRowsItemText: 'All',
        }}
        onChangePage={onPageChange}
        onChangeRowsPerPage={onSizePerPageList}
        striped
        bordered={bordered}
        persistTableHead
      />
    </div>
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
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
    }),
  ).isRequired,
  pageSize: PropTypes.number,
  onPageChange: PropTypes.func.isRequired,
  onSizePerPageList: PropTypes.func.isRequired,
  columns: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired, // `name` replaces `text`
      selector: PropTypes.func.isRequired, // `selector` replaces `dataField`
      fm: PropTypes.string,
      format: PropTypes.func, // `format` replaces `formatter`
      style: PropTypes.object,
    }),
  ).isRequired,
  isFeatureVisible: PropTypes.func.isRequired,
};

export default fmConnect(BootstrapTableWrapper);
