import { API_ROOT, OCE } from '../state/oce-state';

export const headerState = OCE.substate({ name: 'headerState' });

export const statsUrl = headerState.input({
  name: 'makueniPPCountEP',
  initial: `${API_ROOT}/makueni/contractStats`,
});

export const statsInfo = headerState.remote({
  name: 'statsInfo',
  url: statsUrl,
});
