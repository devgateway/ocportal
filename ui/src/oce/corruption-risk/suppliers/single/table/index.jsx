import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import BootstrapTableWrapper from '../../../archive/bootstrap-table-wrapper';
import { getFlaggedReleases } from './api';
import './style.scss';
import { Link } from 'react-router-dom';

const Table = ({ filters }) => {
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(20);

  const [data, setData] = useState([]);
  const [count, setCount] = useState(0);

  useEffect(() => {
    const params = {
      ...filters,
      pageSize,
      pageNumber: page - 1,
    };
    getFlaggedReleases(params).then(([data, count]) => {
      setData(data);
      setCount(count);
    });
  }, [filters, pageSize, page]);

  const { t } = useTranslation();

  const formatTypes = (_, row) => (
    <div>
      {row.types.map((datum) => {
        const translated = t(`crd:corruptionType:${datum.type}:name`);
        const line = `${translated}: ${datum.count}`;
        return <div key={datum.type}>{line}</div>;
      })}
    </div>
  );

  const formatFlags = (_, row) => (
    <div>
      {row.flags.map((indicator) => (
        <div key={indicator}>
          {t(`crd:indicators:${indicator}:name`)}
        </div>
      ))}
    </div>
  );

  const formatDate = (date) => new Date(date).toLocaleDateString();

  const formatPE = (PEName, { PEId }) => (
    <Link to={`/portal/crd/procuring-entity/${PEId}`}>
      {PEName}
    </Link>
  );

  return (
    <BootstrapTableWrapper
      data={data}
      page={page}
      pageSize={pageSize}
      onPageChange={(newPage) => setPage(newPage)}
      onSizePerPageList={(newPageSize) => setPageSize(newPageSize)}
      count={count}
      containerClass="supplier-procurements-table"
      columns={[{
        text: t('crd:contracts:baseInfo:procuringEntityName'),
        dataField: 'PEName',
        fm: 'crd.supplier.procurements.col.procuringEntityName',
        className: 'pe-name',
        columnClassName: 'pe-name',
        formatter: formatPE,
        headerStyle: {
          width: '20%',
        },
      }, {
        text: t('crd:contracts:baseInfo:buyerName'),
        dataField: 'buyerName',
        fm: 'crd.supplier.procurements.col.buyerName',
        className: 'buyer-name',
        columnClassName: 'buyer-name',
        headerStyle: {
          width: '20%',
        },
      }, {
        text: t('crd:contracts:list:awardAmount'),
        dataField: 'awardAmount',
        fm: 'crd.supplier.procurements.col.awardAmount',
      }, {
        text: t('crd:contracts:baseInfo:awardDate'),
        dataField: 'awardDate',
        fm: 'crd.supplier.procurements.col.awardDate',
        formatter: formatDate,
      }, {
        text: t('crd:supplier:table:nrBidders'),
        dataField: 'nrBidders',
        fm: 'crd.supplier.procurements.col.nrBidders',
        headerStyle: {
          width: '10%',
        },
      }, {
        text: t('crd:procurementsTable:noOfFlags'),
        dataField: 'dummy1',
        fm: 'crd.supplier.procurements.col.nrFlags',
        formatter: formatTypes,
        headerStyle: {
          width: '25%',
        },
      }, {
        text: t('crd:supplier:table:flagName'),
        dataField: 'dummy2',
        fm: 'crd.supplier.procurements.col.flags',
        formatter: formatFlags,
        headerStyle: {
          width: '25%',
        },
      }]}
    />
  );
};

export default Table;
