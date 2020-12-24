import { fetch } from '../../api/Api';
import { awardCountsMapper, tenderCountsMapper } from '../api';

export const getTenderAndAwardCounts = (params) => Promise.all([
  fetch('/procuringEntitiesTendersCount', params).then(tenderCountsMapper),
  fetch('/procuringEntitiesAwardsCount', params).then(awardCountsMapper),
]);
