import SingleSelect from './inputs/single-select';

class LocationTypeSelect extends SingleSelect {
  getTitle() {
    return this.t('filters:locationType:title');
  }

  getId(option) {
    return option.get('_id');
  }

  getLabel(option) {
    return option.get('_id').toUpperCase();
  }
}


LocationTypeSelect.ENDPOINT = '/api/ocds/locationTypes/all';

export default LocationTypeSelect;
