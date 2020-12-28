import { fetch } from '../../../../api/Api';

const findActiveAward = (awards) => awards.find(
  (award) => award.status === 'active',
);

const getAwardAmount = (awards) => {
  const award = findActiveAward(awards);
  const { value } = award;
  return `${value.amount} ${value.currency}`;
};

const getAwardDate = (awards) => {
  const award = findActiveAward(awards);
  return award.date;
};

const mapFlaggedReleases = (data) => data.map((datum) => ({
  id: datum.id,
  PEName: datum.tender.procuringEntity.name,
  PEId: datum.tender.procuringEntity.id,
  awardAmount: getAwardAmount(datum.awards),
  awardDate: getAwardDate(datum.awards),
  nrBidders: datum.tender.numberOfTenderers,
  types: datum.flags.flaggedStats,
  flags: Object.keys(datum.flags).filter((key) => datum.flags[key].value),
}));

// eslint-disable-next-line import/prefer-default-export
export const getFlaggedReleases = (params) => Promise.all([
  fetch('/flaggedRelease/all', params).then(mapFlaggedReleases),
  fetch('/flaggedRelease/count', params),
]);
