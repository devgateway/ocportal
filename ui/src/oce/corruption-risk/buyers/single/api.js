import { fetch } from '../../../api/Api';
import {
  getContractsCount,
  getFlaggedNrData,
  getFlagsCount,
  getUnflaggedContractsCount,
  mapWinsAndFlags,
  procurementMapper,
} from '../../api';

const getInfo = (filters) => fetch(`/ocds/organization/buyer/id/${filters.buyerId}`);

const getPrs = (filters) => fetch('/procuringEntitiesForBuyers', filters);

const getWinsAndFlagsData = (filters) => fetch('/supplierWinsPerBuyer', filters)
  .then(mapWinsAndFlags('supplierName'));

const getProcurementsByStatusData = (filters) => fetch('/procurementsByTenderStatusBuyer', filters)
  .then(procurementMapper);

const getProcurementsByMethodData = (filters) => fetch('/procurementsByProcurementMethodBuyer', filters)
  .then(procurementMapper);

export const fetchAllInfo = (filters) => Promise.all([
  getInfo(filters),
  getFlagsCount(filters),
  getPrs(filters),
  getContractsCount(filters),
  getUnflaggedContractsCount(filters),
  getFlaggedNrData(filters),
  getWinsAndFlagsData(filters),
  getProcurementsByStatusData(filters),
  getProcurementsByMethodData(filters),
]).then(([info, flagsCount, prs, contractsCount, unflaggedContractsCount, flaggedNrData, winsAndFlagsData,
  procurementsByStatusData, procurementsByMethodData]) => ({
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
