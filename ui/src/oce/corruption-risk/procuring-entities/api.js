import { fetch } from '../../api/Api';
import { awardCountsMapper, tenderCountsMapper } from '../api';

// eslint-disable-next-line import/prefer-default-export
export const getTenderAndAwardCounts = (params) => Promise.all([
  fetch('/procuringEntitiesTendersCount', params).then(tenderCountsMapper),
  fetch('/procuringEntitiesAwardsCount', params).then(awardCountsMapper),
]);
