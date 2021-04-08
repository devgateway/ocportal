import { Map, TileLayer, ZoomControl } from 'react-leaflet';
import Control from 'react-leaflet-control';
import React from 'react';
import L from 'leaflet';
import { pluck } from '../../tools';
import Cluster from './cluster';
import Location from './location';
import Visualization from '../../visualization';
import './style.scss';
import backendFilterable from '../../backend-year-filterable';

const swap = ([a, b]) => [b, a];

class MapVisual extends backendFilterable(Visualization) {
  constructor(props) {
    super(props);
    this.state = {
      locationType: 'subcounty',
      center: { lat: 0, lng: 0 },
    };
  }

  getMaxAmount() {
    return Math.max(0, ...this.getData()
      .map(pluck('amount')));
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
      const newCenter = L.latLngBounds(data
        .map(pluck('coords'))
        .map(swap))
        .getCenter();
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
      <Map center={center} zoom={zoom} zoomControl={false} dragging={!L.Browser.mobile} tap={false}>
        {this.getTiles()}
        <Cluster maxAmount={this.getMaxAmount()}>
          {this.getData()
            .map((location) => (
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
        <Control position="topleft">
          <button
            onClick={() => this.updateLocationButtonState('ward')}
            className={this.computeLocationButtonClass(locationType, 'ward')}
          >
            Wards
          </button>
        </Control>
        <Control position="topleft">
          <button
            onClick={() => this.updateLocationButtonState('subcounty')}
            className={this.computeLocationButtonClass(locationType, 'subcounty')}
          >
            Subcounties
          </button>
        </Control>
        <ZoomControl position="topright" />

      </Map>
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
