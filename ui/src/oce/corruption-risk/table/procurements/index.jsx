import React, { useEffect } from 'react';
import { useImmer } from 'use-immer';
import { tCreator } from '../../../translatable';
import BootstrapTableWrapper from '../../archive/bootstrap-table-wrapper';
import { mkContractLink } from '../../tools';
import { getFlaggedReleases } from './api';
import { setImmer } from '../../../tools';

const Table = ({ filters, navigate, translations }) => {
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
        formatter: mkContractLink(navigate),
        headerStyle: {
          width: '20%',
        },
      }, {
        text: 'OCID',
        dataField: 'id',
        formatter: mkContractLink(navigate),
      }, {
        text: 'Award status',
        dataField: 'awardStatus',
      }, {
        text: 'Tender amount',
        dataField: 'tenderAmount',
      }, {
        text: t('crd:contracts:list:awardAmount'),
        dataField: 'awardAmount',
      }, {
        text: 'Number of bidders',
        dataField: 'nrBidders',
      }, {
        text: 'Number of flags',
        dataField: 'nrFlags',
      }, {
        text: t('crd:supplier:table:flagName'),
        dataField: 'flags',
        formatter: formatFlags,
        headerStyle: {
          width: '20%',
        },
      }]}
    />
  );
};

export default Table;
