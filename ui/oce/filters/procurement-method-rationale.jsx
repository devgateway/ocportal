import MultipleSelect from './inputs/multiple-select';

export default class ProcurementMethodRationale extends MultipleSelect {
  getTitle() {
    return this.t('filters:tabs:procurementMethodRationale:title');
  }

  getId(option) {
    return option.get('_id');
  }

  getLabel(option) {
    return option.get('_id');
  }

  transform(data) {
    return data.filter(({ _id }) => !!_id);
  }
}

ProcurementMethodRationale.ENDPOINT = '/api/ocds/procurementMethodRationale/all';
