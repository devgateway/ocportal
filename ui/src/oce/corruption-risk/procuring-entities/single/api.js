import {fetch} from "../../../api/Api";
import {
  getContractsCount,
  getFlaggedNrData,
  getFlagsCount,
  getUnflaggedContractsCount,
  mapWinsAndFlags,
  procurementMapper
} from "../../api";

const getInfo = filters => fetch(`/ocds/organization/procuringEntity/id/${filters.procuringEntityId}`);

const getBuyers = filters => fetch('/buyersForProcuringEntities', filters);

const getWinsAndFlagsData = filters => fetch('/supplierWinsPerProcuringEntity', filters)
  .then(mapWinsAndFlags('supplierName'));

const getProcurementsByStatusData = filters =>
  fetch('/procurementsByTenderStatus', filters)
    .then(procurementMapper);

const getProcurementsByMethodData = filters =>
  fetch('/procurementsByProcurementMethod', filters)
    .then(procurementMapper);

export const fetchAllInfo = filters =>
  Promise.all([
    getInfo(filters),
    getFlagsCount(filters),
    getBuyers(filters),
    getContractsCount(filters),
    getUnflaggedContractsCount(filters),
    getFlaggedNrData(filters),
    getWinsAndFlagsData(filters),
    getProcurementsByStatusData(filters),
    getProcurementsByMethodData(filters)
  ]).then(([info, flagsCount, buyers, contractsCount, unflaggedContractsCount, flaggedNrData, winsAndFlagsData,
      procurementsByStatusData, procurementsByMethodData]) => ({
    info,
    flagsCount,
    buyers,
    contractsCount,
    unflaggedContractsCount,
    flaggedNrData,
    winsAndFlagsData,
    procurementsByStatusData,
    procurementsByMethodData
  }));
