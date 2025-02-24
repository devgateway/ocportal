import {
  TileLayer, ZoomControl, useMap, MapContainer,
} from 'react-leaflet';
import React, { useEffect } from 'react';
import L from 'leaflet';
import { pluck } from '../../tools';
import Cluster from './cluster';
import Location from './location';
import Visualization from '../../visualization';
import './style.scss';
import backendFilterable from '../../backend-year-filterable';

const swap = ([a, b]) => [b, a];

const CustomControl = ({
  position, onClick, buttonClass, children,
}) => {
  const map = useMap();

  useEffect(() => {
    const control = L.control({ position });

    control.onAdd = () => {
      const div = L.DomUtil.create('div', 'leaflet-control-container');
      const button = L.DomUtil.create('button', buttonClass, div);
      button.innerHTML = children;
      button.onclick = onClick;

      return div;
    };

    control.addTo(map);

    return () => {
      control.remove();
    };
  }, [map, position, onClick, buttonClass, children]);

  return null;
};

class MapVisual extends backendFilterable(Visualization) {
  constructor(props) {
    super(props);
    this.state = {
      locationType: 'subcounty',
      center: { lat: 0, lng: 0 },
    };
  }

  getMaxAmount() {
    return Math.max(0, ...this.getData().map(pluck('amount')));
  }

  getTiles() {
    return (
      <TileLayer
        url="//{s}.tile.osm.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="//osm.org/copyright">OpenStreetMap</a> contributors'
      />
    );
  }

  componentDidMount() {
    this.handleUpdate(['subcounty']);
    this.computeCenter();
  }

  componentWillUnmount() {
    this.handleUpdate([]);
  }

  componentDidUpdate(prevProps) {
    super.componentDidUpdate(prevProps);
    this.computeCenter();
  }

  computeLocationButtonClass(locationType, buttonType) {
    return `btn ${locationType === buttonType ? 'btn-primary' : ''}`;
  }

  updateLocationButtonState(buttonType) {
    console.log('update location button state');
    this.setState({ locationType: buttonType });
    this.handleUpdate([buttonType]);
  }

  handleUpdate(locationTypeValue) {
    const { onUpdate } = this.props;
    onUpdate({
      locationType: locationTypeValue,
    });
  }

  computeCenter() {
    const data = this.getData();
    if (data && data.length > 0) {
      const newCenter = L.latLngBounds(data.map(pluck('coords')).map(swap)).getCenter();
      const { center } = this.state;
      if (center.lat !== newCenter.lat || center.lng !== newCenter.lng) {
        this.setState({ center: { lat: newCenter.lat, lng: newCenter.lng } });
      }
    }
  }

  render() {
    const {
      t, filters, years, styling, months, monthly, zoom,
    } = this.props;
    const { locationType, center } = this.state;

    return (
      <MapContainer center={center} zoom={zoom} zoomControl={false} dragging={!L.Browser.mobile} tap={false}>
        {this.getTiles()}
        <Cluster maxAmount={this.getMaxAmount()}>
          {this.getData().map((location) => (
            <this.constructor.Location
              key={location._id}
              position={location.coords.reverse()}
              maxAmount={this.getMaxAmount()}
              data={location}
              t={t}
              filters={filters}
              years={years}
              months={months}
              monthly={monthly}
              styling={styling}
            />
          ))}
        </Cluster>
        <CustomControl position="topleft" onClick={() => this.updateLocationButtonState('ward')} buttonClass={this.computeLocationButtonClass(locationType, 'ward')}>
          Wards
        </CustomControl>
        <CustomControl position="topleft" onClick={() => this.updateLocationButtonState('subcounty')} buttonClass={this.computeLocationButtonClass(locationType, 'subcounty')}>
          Subcounties
        </CustomControl>
        <ZoomControl position="topright" />
      </MapContainer>
    );
  }
}

MapVisual.propTypes = {};
MapVisual.defaultProps = {
  zoom: 1,
};
MapVisual.computeComparisonYears = null;
MapVisual.Location = Location;

export default MapVisual;
