import SingleSelect from './inputs/single-select';

class BuyerSelect extends SingleSelect {
  getTitle() {
    return this.t('filters:buyer:title');
  }
}

BuyerSelect.ENDPOINT = '/api/ocds/organization/buyer/all';

export default BuyerSelect;
