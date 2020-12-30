import React, { useEffect } from 'react';
import { useImmer } from 'use-immer';
import PropTypes from 'prop-types';
import { tCreator } from '../../../translatable';
import BootstrapTableWrapper from '../../archive/bootstrap-table-wrapper';
import { mkContractLink } from '../../tools';
import { getFlaggedReleases } from './api';
import { setImmer } from '../../../tools';

const Table = ({
  filters, navigate, translations, fmPrefix,
}) => {
  const t = tCreator(translations);

  const formatFlags = (data) => (
    <div>
      {data.map((indicator) => (
        <div key={indicator}>
          &#9679;
          {' '}
          {t(`crd:indicators:${indicator}:name`)}
        </div>
      ))}
    </div>
  );

  const [page, updatePage] = useImmer(1);
  const [pageSize, updatePageSize] = useImmer(20);

  const [data, updateData] = useImmer([]);
  const [count, updateCount] = useImmer(0);

  useEffect(() => {
    const params = {
      ...filters,
      pageNumber: page - 1,
      pageSize,
    };
    getFlaggedReleases(params)
      .then(([data, count]) => {
        updateData(() => data);
        updateCount(() => count);
      });
  }, [filters, page, pageSize]);

  return (
    <BootstrapTableWrapper
      data={data}
      page={page}
      pageSize={pageSize}
      onPageChange={setImmer(updatePage)}
      onSizePerPageList={setImmer(updatePageSize)}
      count={count}
      columns={[{
        text: 'Tender name',
        dataField: 'name',
        fm: `${fmPrefix}.tenderName`,
        formatter: mkContractLink(navigate),
        headerStyle: {
          width: '20%',
        },
      }, {
        text: 'OCID',
        dataField: 'id',
        fm: `${fmPrefix}.ocid`,
        formatter: mkContractLink(navigate),
      }, {
        text: 'Award status',
        dataField: 'awardStatus',
        fm: `${fmPrefix}.awardStatus`,
      }, {
        text: 'Tender amount',
        dataField: 'tenderAmount',
        fm: `${fmPrefix}.tenderAmount`,
      }, {
        text: t('crd:contracts:list:awardAmount'),
        dataField: 'awardAmount',
        fm: `${fmPrefix}.awardAmount`,
      }, {
        text: 'Number of bidders',
        dataField: 'nrBidders',
        fm: `${fmPrefix}.nrBidders`,
      }, {
        text: 'Number of flags',
        dataField: 'nrFlags',
        fm: `${fmPrefix}.nrFlags`,
      }, {
        text: t('crd:supplier:table:flagName'),
        dataField: 'flags',
        fm: `${fmPrefix}.flags`,
        formatter: formatFlags,
        headerStyle: {
          width: '20%',
        },
      }]}
    />
  );
};

Table.propTypes = {
  navigate: PropTypes.func.isRequired,
  fmPrefix: PropTypes.string.isRequired,
};

export default Table;
