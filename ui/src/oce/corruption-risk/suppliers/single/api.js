import { fetch } from '../../../api/Api';
import { getFlaggedNrData, mapWinsAndFlags } from '../../api';

const getWinsAndFlagsData = (filters) => fetch('/supplierWinsPerProcuringEntity', filters)
  .then(mapWinsAndFlags('procuringEntityName'));

const getWinsAndFlagsPerBuyerData = (filters) => fetch('/supplierWinsPerBuyer', filters)
  .then(mapWinsAndFlags('buyerName'));

// eslint-disable-next-line import/prefer-default-export
export const fetchAllInfo = (filters) => Promise.all([
  getFlaggedNrData(filters),
  getWinsAndFlagsData(filters),
  getWinsAndFlagsPerBuyerData(filters),
]).then(([flaggedNrData, winsAndFlagsData, winsAndFlagsPerBuyerData]) => ({
  flaggedNrData,
  winsAndFlagsData,
  winsAndFlagsPerBuyerData,
}));
