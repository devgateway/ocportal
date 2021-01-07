import { Popup } from 'react-leaflet';
import Location from './marker';
import Component from '../../../pure-render-component';
import translatable from '../../../translatable';

export default class LocationWrapper extends translatable(Component) {
  render() {
    const { amount, name } = this.props.data;
    return (
      <Location {...this.props}>
        <Popup>
          <div>
            <h3>{name}</h3>
            <p>
              <strong>{this.t('general:amountInVND')}</strong>
              {' '}
              {amount.toLocaleString()}
            </p>
          </div>
        </Popup>
      </Location>
    );
  }
}
