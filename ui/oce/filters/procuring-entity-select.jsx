import SingleSelect from './inputs/single-select';

class ProcuringEntitySelect extends SingleSelect {
  getTitle() {
    return this.t('filters:procuringEntity:title');
  }
}


ProcuringEntitySelect.ENDPOINT = '/api/ocds/organization/procuringEntity/all';

export default ProcuringEntitySelect;
