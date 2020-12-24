import cn from 'classnames';
import { Set } from 'immutable';
import React from 'react';
import Tab from '../index';
import TenderLocations from '../../visualizations/map/tender-locations';

class LocationTab extends Tab {
  static getName(t) {
    return t('tabs:location:title');
  }

  static computeYears(data) {
    if (!data) return Set();
    return this.LAYERS.reduce((years, visualization, index) => (visualization.computeYears
      ? years.union(visualization.computeYears(data.get(index)))
      : years),
    Set());
  }

  constructor(props) {
    super(props);
    this.state = {
      currentLayer: 0,
      dropdownOpen: false,
      switcherPos: {
        top: 0,
        left: 0,
      },
    };
  }

  maybeGetSwitcher() {
    const { LAYERS } = this.constructor;
    const { switcherPos } = this.state;
    if (this.constructor.LAYERS.length > 1) {
      const { currentLayer, dropdownOpen } = this.state;
      return (
        <div className="layer-switcher" style={switcherPos}>
          <div
            className={cn('dropdown', { open: dropdownOpen })}
            onClick={(e) => this.setState({ dropdownOpen: !dropdownOpen })}
          >
            <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1">
              {LAYERS[currentLayer].getLayerName(this.t.bind(this))}
              {' '}
              <span className="caret" />
            </button>
            <ul className="dropdown-menu">
              {LAYERS.map((layer, index) => (
                <li key={index}>
                  <a onClick={(e) => this.setState({ currentLayer: index })}>
                    {LAYERS[index].getLayerName(this.t.bind(this))}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        </div>
      );
    }
  }

  componentDidMount() {
    super.componentDidMount();
    const zoom = document.querySelector('.leaflet-control-zoom');

    this.setState({
      switcherPos: {
        // top: zoom.offsetTop,
        // left: zoom.offsetLeft + zoom.offsetWidth + 10
      },
    });
  }

  render() {
    const { currentLayer } = this.state;
    const {
      data, requestNewData, translations, filters, years, styling, onUpdate,
    } = this.props;

    const { LAYERS, CENTER, ZOOM } = this.constructor;
    const Map = LAYERS[currentLayer];
    return (
      <div className="col-sm-12 content map-content">
        {this.maybeGetSwitcher()}
        <Map
          {...this.props}
          data={data.get(currentLayer)}
          requestNewData={(_, data) => requestNewData([currentLayer], data)}
          translations={translations}
          filters={filters}
          onUpdate={onUpdate}
          years={years}
          styling={styling}
          center={CENTER}
          zoom={ZOOM}
        />
      </div>
    );
  }
}

LocationTab.icon = 'planning';
LocationTab.computeComparisonYears = null;
LocationTab.LAYERS = [TenderLocations];
LocationTab.CENTER = [14.5, 105];
LocationTab.ZOOM = 10;

export default LocationTab;
