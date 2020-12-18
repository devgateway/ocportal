import {fetch} from "../../../api/Api";

const getFlaggedNrData = async filters => {
  const indicatorTypesMapping = await fetch('/indicatorTypesMapping');

  return Promise.all(
    Object.keys(indicatorTypesMapping).map(indicatorId =>
      fetch(`/flags/${indicatorId}/count`, filters)
        .then(data => {
          if (!data[0]) return null;
          return {
            indicatorId,
            count: data[0].count,
            types: indicatorTypesMapping[indicatorId].types
          }
        })
    )
  ).then(data =>
    data.filter(datum => !!datum).sort((a, b) => b.count - a.count)
  );
}

const getFlagsCount = filters => fetch('/totalFlags', filters)
  .then(data => data.length === 0 ? 0 : data[0].flaggedCount);

const getWinsAndFlagsData = filters => fetch('/supplierWinsPerBuyer', filters)
  .then(data => data.map(datum => {
    return {
      name: datum.supplierName,
      wins: datum.count,
      flags: datum.countFlags,
    }
  }));

const getProcurementsByStatusData = filters => fetch('/procurementsByTenderStatusBuyer', filters)
  .then(data => data.map(
    datum => ({
      status: datum.tenderStatus,
      count: datum.count,
    })
  ).sort((a, b) => b.count - a.count));

const getProcurementsByMethodData = filters => fetch('/procurementsByProcurementMethodBuyer', filters)
  .then(data => data.map(
    datum => ({
      status: datum.tenderStatus,
      count: datum.count,
    })
  ).sort((a, b) => b.count - a.count));

const getInfo = filters => fetch(`/ocds/organization/buyer/id/${filters.buyerId}`);

function getPrs(filters) {
  return fetch('/procuringEntitiesForBuyers', filters);
}

function getContractsCount(filters) {
  return fetch('/flaggedRelease/count', filters);
}

function getUnflaggedContractsCount(filters) {
  return fetch('/ocds/release/count', filters);
}

export const fetchAllInfo = async (filters) => {
  return Promise.all([
    getInfo(filters),
    getFlagsCount(filters),
    getPrs(filters),
    getContractsCount(filters),
    getUnflaggedContractsCount(filters),
    getFlaggedNrData(filters),
    getWinsAndFlagsData(filters),
    getProcurementsByStatusData(filters),
    getProcurementsByMethodData(filters)
  ]).then(([info,
             flagsCount,
             prs,
             contractsCount,
             unflaggedContractsCount,
             flaggedNrData,
             winsAndFlagsData,
             procurementsByStatusData,
             procurementsByMethodData]) => ({
    info,
    flagsCount,
    prs,
    contractsCount,
    unflaggedContractsCount,
    flaggedNrData,
    winsAndFlagsData,
    procurementsByStatusData,
    procurementsByMethodData,
  }));
};
