import { Set } from 'immutable';
import { CRD, datefulFilters, API_ROOT } from '../../state/oce-state';

export const BuyersIds = CRD.input({
  name: 'BuyersIds'
});

export const BuyersFilters = CRD.mapping({
  name: 'BuyersFilters',
  deps: [datefulFilters, BuyersIds],
  mapper: (filters, PEId) =>
    filters.update('buyerId', Set(), PEIds => PEIds.add(PEId))
});

const BuyersTendersCountEP = CRD.input({
  name: 'BuyersTendersCountEP',
  initial: `${API_ROOT}/buyersTendersCount`,
});

const BuyersAwardsCountEP = CRD.input({
  name: 'BuyersAwardsCountEP',
  initial: `${API_ROOT}/buyersAwardsCount`,
});

const BuyersTendersCountRaw = CRD.remote({
  name: 'BuyersTendersCountRaw',
  url: BuyersTendersCountEP,
  params: BuyersFilters,
});

export const BuyersTendersCount = CRD.mapping({
  name: 'BuyersTendersCount',
  deps: [BuyersTendersCountRaw],
  mapper: raw => {
    const result = {};
    raw.forEach(({ _id, tenderCount }) => {
      result[_id] = tenderCount;
    });
    return result;
  },
});

const BuyersAwardsCountRaw = CRD.remote({
  name: 'BuyersAwardsCountRaw',
  url: BuyersAwardsCountEP,
  params: BuyersFilters,
});

export const BuyersAwardsCount = CRD.mapping({
  name: 'BuyersAwardsCount',
  deps: [BuyersAwardsCountRaw],
  mapper: raw => {
    const result = {};
    raw.forEach(({ _id, awardCount }) => {
      result[_id] = awardCount;
    });
    return result;
  },
});
