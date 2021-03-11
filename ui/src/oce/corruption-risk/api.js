import { fetch } from '../api/Api';

export const getFlagsCount = (filters) => fetch('/totalFlags', filters)
  .then((data) => (data.length === 0 ? 0 : data[0].flaggedCount));

export const getUnflaggedContractsCount = (filters) => fetch('/ocds/release/count', filters);

export const getContractsCount = (filters) => fetch('/flaggedRelease/count', filters);

export const tenderCountsMapper = (data) => data.reduce((res, { _id, tenderCount }) => {
  // eslint-disable-next-line no-param-reassign
  res[_id] = tenderCount;
  return res;
}, {});

export const awardCountsMapper = (data) => data.reduce((res, { _id, awardCount }) => {
  // eslint-disable-next-line no-param-reassign
  res[_id] = awardCount;
  return res;
}, {});

export const procurementMapper = (data) => data.map(
  (datum) => ({
    status: datum.tenderStatus,
    count: datum.count,
  }),
).sort((a, b) => b.count - a.count);

export const mapWinsAndFlags = (prop) => (data) => data.map((datum) => ({
  name: datum[prop],
  wins: datum.count,
  flags: datum.countFlags,
}));

export const getFlaggedNrData = async (filters) => {
  const indicatorTypesMapping = await fetch('/indicatorTypesMapping');

  const filtersWithFlags = {
    ...filters,
    flags: Object.keys(indicatorTypesMapping),
  };

  return fetch('/flags/count', filtersWithFlags).then((data) => Object.entries(data)
    .filter(([, counts]) => counts.length > 0)
    .map(([indicatorId, counts]) => ({
      indicatorId,
      count: counts[0].count,
      types: indicatorTypesMapping[indicatorId].types,
    }))
    .sort((a, b) => b.count - a.count));
};
