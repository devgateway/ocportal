import React, { useEffect } from 'react';
import { useImmer } from 'use-immer';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import BootstrapTableWrapper from '../../archive/bootstrap-table-wrapper';
import { mkContractLink } from '../../tools';
import { getFlaggedReleases } from './api';
import { setImmer } from '../../../tools';

const Table = ({
  filters, fmPrefix,
}) => {
  const { t } = useTranslation();

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
        text: t('crd:procurements:table:tenderName'),
        dataField: 'name',
        fm: `${fmPrefix}.tenderName`,
        formatter: mkContractLink,
        headerStyle: {
          width: '20%',
        },
      }, {
        text: t('crd:procurements:table:ocid'),
        dataField: 'ocid',
        fm: `${fmPrefix}.ocid`,
        formatter: mkContractLink,
      }, {
        text: t('crd:procurements:table:awardStatus'),
        dataField: 'awardStatus',
        fm: `${fmPrefix}.awardStatus`,
      }, {
        text: t('crd:procurements:table:tenderAmount'),
        dataField: 'tenderAmount',
        fm: `${fmPrefix}.tenderAmount`,
      }, {
        text: t('crd:procurements:table:awardAmount'),
        dataField: 'awardAmount',
        fm: `${fmPrefix}.awardAmount`,
      }, {
        text: t('crd:procurements:table:nrOfBidders'),
        dataField: 'nrBidders',
        fm: `${fmPrefix}.nrBidders`,
      }, {
        text: t('crd:procurements:table:nrOfFlags'),
        dataField: 'nrFlags',
        fm: `${fmPrefix}.nrFlags`,
      }, {
        text: t('crd:procurements:table:flagName'),
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
  fmPrefix: PropTypes.string.isRequired,
};

export default Table;
