import { fetch } from '../../api/Api';
import { awardCountsMapper, tenderCountsMapper } from '../api';

// eslint-disable-next-line import/prefer-default-export
export const getBuyersTenderAndAwardCounts = (params) => Promise.all([
  fetch('/buyersTendersCount', params).then(tenderCountsMapper),
  fetch('/buyersAwardsCount', params).then(awardCountsMapper),
]);
