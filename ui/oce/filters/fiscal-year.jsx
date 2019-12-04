import MultipleSelect from './inputs/multiple-select';

export default class FiscalYear extends MultipleSelect {
  getTitle() {
    return this.t('filters:tabs:fiscalYear:title');
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

FiscalYear.ENDPOINT = '/api/ocds/fiscalYear/all';
