import { fetch } from '../../../api/Api';
import { getFlaggedNrData, mapWinsAndFlags } from '../../api';

const getWinsAndFlagsData = (filters) => fetch('/supplierWinsPerProcuringEntity', filters)
  .then(mapWinsAndFlags('procuringEntityName'));

export const fetchAllInfo = (filters) => Promise.all([
  getFlaggedNrData(filters),
  getWinsAndFlagsData(filters),
]).then(([flaggedNrData, winsAndFlagsData]) => ({
  flaggedNrData,
  winsAndFlagsData,
}));
