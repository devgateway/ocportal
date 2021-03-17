import { Popup } from 'react-leaflet';
import Location from './marker';
import Component from '../../../pure-render-component';

export default class LocationWrapper extends Component {
  render() {
    const { amount, name } = this.props.data;
    const { t } = this.props;
    return (
      <Location {...this.props}>
        <Popup>
          <div>
            <h3>{name}</h3>
            <p>
              <strong>{t('general:amountInVND')}</strong>
              {' '}
              {amount.toLocaleString()}
            </p>
          </div>
        </Popup>
      </Location>
    );
  }
}
