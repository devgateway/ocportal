import React, {useEffect, useState} from "react";
import {tCreator} from '../../../../translatable';
import BootstrapTableWrapper from '../../../archive/bootstrap-table-wrapper';
import {getFlaggedReleases} from "./api";
import './style.scss';

const Table = ({filters, translations}) => {

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(20);

  const [data, setData] = useState([]);
  const [count, setCount] = useState(0);

  useEffect(() => {
    const params = {
      ...filters,
      pageSize,
      pageNumber: page - 1
    };
    getFlaggedReleases(params).then(([data, count]) => {
      setData(data);
      setCount(count);
    });
  }, [filters, pageSize, page]);

  const t = tCreator(translations);

  const formatTypes = data => {
    return (
      <div>
        {data.map(datum => {
          const translated = t(`crd:corruptionType:${datum.type}:name`);
          return <div>{translated}: {datum.count}</div>;
        })}
      </div>
    );
  };

  const formatFlags = (data) => {
    return (
      <div>
        {data.map(indicator => (
          <div>
           {t(`crd:indicators:${indicator}:name`)}
          </div>
        ))}
      </div>
    );
  };

  const formatDate = (date) => {
    return new Date(date).toLocaleDateString();
  };

  const formatPE = (PEName, { PEId }) => {
    return (
      <a href={`#!/crd/procuring-entity/${PEId}`}>
        {PEName}
      </a>
    );
  };

  return (
    <BootstrapTableWrapper
      data={data}
      bordered
      page={page}
      pageSize={pageSize}
      onPageChange={newPage => setPage(newPage)}
      onSizePerPageList={newPageSize => setPageSize(newPageSize)}
      count={count}
      containerClass="supplier-procurements-table"
      columns={[{
          title: t('crd:contracts:baseInfo:procuringEntityName'),
          dataField: 'PEName',
          className: 'pe-name',
          columnClassName: 'pe-name',
          dataFormat: formatPE,
      }, {
          title: t('crd:contracts:list:awardAmount'),
          dataField: 'awardAmount',
      }, {
          title: t('crd:contracts:baseInfo:awardDate'),
          dataField: 'awardDate',
          dataFormat: formatDate,
      }, {
          title: t('crd:supplier:table:nrBidders'),
          dataField: 'nrBidders',
          width: '10%',
      }, {
          title: t('crd:procurementsTable:noOfFlags'),
          dataField: 'types',
          dataFormat: formatTypes,
          width: '25%',
      }, {
          title: t('crd:supplier:table:flagName'),
          dataField: 'flags',
          dataFormat: formatFlags,
          width: '25%',
      }]}
    />
  );
};

export default Table;
