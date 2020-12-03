import Map from "../index.jsx";
import Location from './location';
import fmConnect from "../../../fm/fm";

class TenderLocations extends Map {
  getData(){
    let data = super.getData();
    if(!data) return [];
    return data
        .map(location => {
          return {
            "_id": location.get('_id'),
            "name": location.get('description'),
            "amount": location.get('totalTendersAmount'),
            "count":  location.get('tendersCount'),
            "totalProjectsAmount":  location.get('totalProjectsAmount'),
            "projectsCount":  location.get('projectsCount'),
            "coords": location.getIn(['geometry', 'coordinates']).toJS()
          }
        })
        .toArray()
  }

  static getLayerName(t){return t('maps:tenderLocations:title')}
}

TenderLocations.endpoint = 'fundingByTenderLocation';
TenderLocations.Location = Location;

export default fmConnect(TenderLocations, 'viz.me.map.tenderLocations');
