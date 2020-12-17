import React, {useEffect, useState} from "react";
import {tCreator} from '../../../../translatable';
import BootstrapTableWrapper from '../../../archive/bootstrap-table-wrapper';
import { mkContractLink } from '../../../tools';
import {getFlaggedReleases} from "./api";

const Table = ({filters, navigate, translations}) => {

  const t = tCreator(translations);

  const formatFlags = (data) => {
    return (
      <div>
        {data.map(indicator => (
          <div>
            &#9679; {t(`crd:indicators:${indicator}:name`)}
          </div>
        ))}
      </div>
    );
  };

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(20);

  const [data, setData] = useState([]);
  const [count, setCount] = useState(0);

  useEffect(() => {
    const params = {
      ...filters,
      pageNumber: page - 1,
      pageSize
    };
    getFlaggedReleases(params)
      .then(([data, count]) => {
        setData(data);
        setCount(count);
      });
  }, [filters, page, pageSize]);

  return (
    <BootstrapTableWrapper
      bordered
      data={data}
      page={page}
      pageSize={pageSize}
      onPageChange={newPage => setPage(newPage)}
      onSizePerPageList={newPageSize => setPageSize(newPageSize)}
      count={count}
      columns={[{
        title: 'Tender name',
        dataField: 'name',
        width: '20%',
        dataFormat: mkContractLink(navigate),
      }, {
        title: 'OCID',
        dataField: 'id',
        dataFormat: mkContractLink(navigate),
      }, {
        title: 'Award status',
        dataField: 'awardStatus',
      }, {
        title: 'Tender amount',
        dataField: 'tenderAmount',
      }, {
        title: t('crd:contracts:list:awardAmount'),
        dataField: 'awardAmount',
      }, {
        title: 'Number of bidders',
        dataField: 'nrBidders',
      }, {
        title: 'Number of flags',
        dataField: 'nrFlags',
      }, {
        title: t('crd:supplier:table:flagName'),
        dataField: 'flags',
        dataFormat: formatFlags,
        width: '20%',
      }]}
    />
  );
}

export default Table;
