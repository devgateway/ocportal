import { fetch } from '../../../api/Api';

const findActiveAward = (awards) => awards.find(
  (award) => award.status === 'active',
);

const getAwardAmount = (awards) => {
  if (!awards) return 0;
  const award = findActiveAward(awards);
  if (!award) return 0;
  const { value } = award;
  return `${value.amount} ${value.currency}`;
};

const getTenderAmount = (datum) => {
  try {
    return `${datum.tender.value.amount} ${datum.tender.value.currency}`;
  } catch (whatever) {
    return 0;
  }
};

const mapFlaggedReleases = (data) => data.map((datum) => ({
  id: datum.ocid,
  ocid: datum.ocid,
  name: datum.tender.title || 'N/A',
  awardStatus: getAwardAmount(datum.awards) ? 'active' : 'unsuccessful',
  tenderAmount: getTenderAmount(datum),
  awardAmount: getAwardAmount(datum.awards),
  nrBidders: datum.tender.numberOfTenderers || 0,
  nrFlags: datum.flags.totalFlagged,
  flags: Object.keys(datum.flags).filter((key) => datum.flags[key].value),
}));

// eslint-disable-next-line import/prefer-default-export
export const getFlaggedReleases = (params) => Promise.all([
  fetch('/flaggedRelease/all', params).then(mapFlaggedReleases),
  fetch('/flaggedRelease/count', params),
]);
