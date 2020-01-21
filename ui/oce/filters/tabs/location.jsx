import Tab from './index';
import LocationTypeSelect from '../location-type-select';

class LocationTab extends Tab {
  render() {
    return (
      <div>
        {this.renderChild(LocationTypeSelect, 'locationType')}
      </div>
    );
  }
}

LocationTab.getName = t => t('filters:tabs:location:title');

export default LocationTab;
