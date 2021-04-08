import Map from '../index';
import Location from './location';

class ContractLocations extends Map {
  getData() {
    const data = super.getData();
    if (!data) return [];
    return data
      .map((location) => ({
        _id: location.get('_id'),
        name: location.get('description'),
        amount: location.get('contractsAmount'),
        count: location.get('contractsCount'),
        coords: location.getIn(['geometry', 'coordinates']).toJS(),
      }))
      .toArray();
  }

  static getLayerName(t) { return t('maps:contractLocations:title'); }
}

ContractLocations.endpoint = 'fundingByContractLocation';
ContractLocations.Location = Location;

export default ContractLocations;
