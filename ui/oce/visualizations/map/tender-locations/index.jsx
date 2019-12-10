import Map from "../index.jsx";
import Location from './location';

class TenderLocations extends Map{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return data
        .groupBy(location => location.getIn(['location', '_id']))
        .map(locations => locations.reduce((reducedLocation, location) => {
          return {
            "_id": location.getIn(['location', '_id']),
            "name": location.getIn(['location', 'description']),
            "amount": reducedLocation.amount + location.get('totalTendersAmount'),
            "count": reducedLocation.count + location.get('tendersCount'),
            "coords": location.getIn(['location', 'geometry', 'coordinates']).toJS()
          }
        }, {
          "amount": 0,
          "count": 0
        }))
        .toArray()
  }

  static getLayerName(t){return t('maps:tenderLocations:title')}
}

TenderLocations.endpoint = 'fundingByTenderLocation';
TenderLocations.Location = Location;

export default TenderLocations;
