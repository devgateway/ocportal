import { API_ROOT, OCE } from '../../state/oce-state';
import { Map } from 'immutable';

// makeuni procurement plan state
export const ppState = OCE.substate({ name: 'makueniPP' });

// makeuni procurement plan filters
export const ppFilters = ppState.input({
  name: 'ppFilters',
  initial: Map(),
});

export const page = ppState.input({
  name: 'page',
  initial: 1,
});

export const pageSize = ppState.input({
  name: 'pageSize',
  initial: 20,
});

const filters = ppState.mapping({
  name: 'filters',
  deps: [ppFilters, page, pageSize],
  mapper: (ppFilters, page, pageSize) => {
    return ppFilters
    .set('pageSize', pageSize)
    .set('pageNumber', page === 0 ? page : page - 1);
  }
});


const ppEP = ppState.input({
  name: 'makueniPPEP',
  initial: `${API_ROOT}/makueni/procurementPlans`,
});

const ppRemote = ppState.remote({
  name: 'ppRemote',
  url: ppEP,
  params: filters,
});

export const ppData = ppState.mapping({
  name: 'ppData',
  deps: [ppRemote],
  mapper: raw =>
    raw.map(datum => {
      return {
        id: datum.id,
        department: datum.department.label,
        fiscalYear: datum.fiscalYear.label,
        formDocs: datum.formDocs
      };
    })
});

const ppCountEP = ppState.input({
  name: 'makueniPPCountEP',
  initial: `${API_ROOT}/makueni/procurementPlansCount`,
});

export const ppCountRemote = ppState.remote({
  name: 'ppCountRemote',
  url: ppCountEP,
  params: filters,
});
