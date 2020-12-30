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
  }

  componentWillUnmount() {
    this.handleUpdate([]);
  }

  computeLocationButtonClass(locationType, buttonType) {
    return `btn ${locationType === buttonType ? 'btn-primary' : ''}`;
  }

  updateLocationButtonState(buttonType) {
    this.setState({ locationType: buttonType });
    this.handleUpdate([buttonType]);
  }

  handleUpdate(locationTypeValue) {
    const { filters, onUpdate } = this.props;
    onUpdate({
      ...filters,
      locationType: locationTypeValue,
    });
  }

  render() {
    const {
      translations, filters, years, styling, months, monthly, zoom, data,
    } = this.props;
    const { locationType } = this.state;
    let center;
    let _zoom;
    if (data) {
      center = L.latLngBounds(this.getData()
        .map(pluck('coords'))
        .map(swap))
        .getCenter();
      _zoom = zoom;
    } else {
      center = [0, 0];
      _zoom = 1;
    }

    return (
      <Map center={center} zoom={_zoom} zoomControl={false} dragging={!L.Browser.mobile} tap={false}>
        {this.getTiles()}
        <Cluster maxAmount={this.getMaxAmount()}>
          {this.getData()
            .map((location) => (
              <this.constructor.Location
                key={location._id}
                position={location.coords.reverse()}
                maxAmount={this.getMaxAmount()}
                data={location}
                translations={translations}
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
MapVisual.computeComparisonYears = null;
MapVisual.Location = Location;

export default MapVisual;
