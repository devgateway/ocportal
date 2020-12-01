import TypeAhead from './inputs/type-ahead';

class Buyer extends TypeAhead {
  getTitle() {
    return this.t('filters:buyer:title');
  }
}

Buyer.ENDPOINT = '/api/ocds/organization/buyer/all';

export default Buyer;
