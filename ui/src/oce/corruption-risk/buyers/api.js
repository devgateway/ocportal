import {fetch} from "../../api/Api";

const tenderCountsMapper = data => data.reduce((res, { _id, tenderCount }) => {
  res[_id] = tenderCount;
  return res;
}, {});

const awardCountsMapper = data => data.reduce((res, { _id, awardCount }) => {
  res[_id] = awardCount;
  return res;
}, {});

export const getBuyersTenderAndAwardCounts = async params => {
  return Promise.all([
    fetch('/buyersTendersCount', params).then(tenderCountsMapper),
    fetch('/buyersAwardsCount', params).then(awardCountsMapper)
  ]);
};
