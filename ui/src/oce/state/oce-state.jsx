import { Map } from 'immutable';
import State from './index';

export const API_ROOT = '/api';

export const OCE = new State({ name: 'oce' });
export const CRD = OCE.substate({ name: 'crd' });

export const filters = CRD.input({
  name: 'filters',
  initial: Map(),
});

export const datelessFilters = CRD.mapping({
  name: 'datelessFilters',
  deps: [filters],
  mapper: ({years:_1, months:_2, datelessFilters}) => datelessFilters,
});

export const datefulFilters = CRD.mapping({
  name: 'datefulFilters',
  deps: [datelessFilters, filters],
  mapper: (datelessFilters, filters) => ({
    ...datelessFilters,
    year: filters.year,
    month: filters.month
  })
});

const indicatorTypesMappingURL = CRD.input({
  name: 'indicatorTypesMappingURL',
  initial: `${API_ROOT}/indicatorTypesMapping`,
});

export const indicatorTypesMapping = CRD.remote({
  name: 'indicatorTypesMapping',
  url: indicatorTypesMappingURL
});

export const indicatorIdsFlat = CRD.mapping({
  name: 'indicatorIdsFlat',
  deps: [indicatorTypesMapping],
  mapper: indicatorTypesMapping => Object.keys(indicatorTypesMapping),
});
