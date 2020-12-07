import SingleSelect from './inputs/single-select';

class SupplierSelect extends SingleSelect {
  getTitle() {
    return this.t('filters:supplier:title');
  }
}

SupplierSelect.ENDPOINT = '/api/ocds/organization/supplier/all';

export default SupplierSelect;
