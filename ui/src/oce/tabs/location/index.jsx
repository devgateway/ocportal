import cn from 'classnames';
import { Set } from 'immutable';
import React from 'react';
import Tab from '../index';
import TenderLocations from '../../visualizations/map/tender-locations';
import PropTypes from 'prop-types';
import ContractLocations from '../../visualizations/map/contract-locations';
import { defaultMemoize } from 'reselect';

const combineFilters = defaultMemoize((a, b) => ({ ...a, ...b }));

class LocationTab extends Tab {
  static getName(t) {
    return t('tabs:location:title');
  }

  static computeYears(data) {
    if (!data) return Set();
    return Object.values(this.LAYERS)
      .map((el) => el.component)
      .reduce(
        (years, visualization, index) => (visualization.computeYears
          ? years.union(visualization.computeYears(data.get(index)))
          : years),
        Set(),
      );
  }

  constructor(props) {
    super(props);
    this.state = {
      currentLayer: Object.keys(this.getVisibleLayers())[0],
      dropdownOpen: false,
      switcherPos: {
        top: 0,
        left: 0,
      },
    };
  }

  maybeGetSwitcher() {
    const { switcherPos } = this.state;
    const { t } = this.props;
    const visibleLayers = this.getVisibleLayers();
    if (Object.keys(visibleLayers).length > 1) {
      const { currentLayer, dropdownOpen } = this.state;
      return (
        <div className="layer-switcher" style={switcherPos}>
          <div
            className={cn('dropdown', { open: dropdownOpen })}
            onClick={() => this.setState({ dropdownOpen: !dropdownOpen })}
          >
            <button className="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1">
              {visibleLayers[currentLayer].component.getLayerName(t)}
              {' '}
              <span className="caret" />
            </button>
            <ul className="dropdown-menu">
              {Object.entries(visibleLayers).map(([layerName, layer]) => (
                <li key={layerName}>
                  <a onClick={() => this.setState({ currentLayer: layerName })}>
                    {layer.component.getLayerName(t)}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        </div>
      );
    }
    return null;
  }

  componentDidMount() {
    super.componentDidMount();
    // const zoom = document.querySelector('.leaflet-control-zoom');

    this.setState({
      switcherPos: {
        // top: zoom.offsetTop,
        // left: zoom.offsetLeft + zoom.offsetWidth + 10
      },
    });
  }

  render() {
    const { currentLayer, extraFilters } = this.state;
    const {
      data, requestNewData, t, filters, years, styling,
    } = this.props;
    const combinedFilters = combineFilters(filters, extraFilters);

    const { LAYERS, CENTER, ZOOM } = this.constructor;
    const Map = LAYERS[currentLayer].component;
    return (
      <div className="col-sm-12 content map-content">
        {this.maybeGetSwitcher()}
        <Map
          {...this.props}
          data={data.get(currentLayer)}
          requestNewData={(_, data) => requestNewData([currentLayer], data)}
          t={t}
          filters={combinedFilters}
          onUpdate={(newExtraFilters) => this.setState({ extraFilters: newExtraFilters })}
          years={years}
          styling={styling}
          center={CENTER}
          zoom={ZOOM}
        />
      </div>
    );
  }

  getVisibleLayers() {
    const { LAYERS } = this.constructor;
    const { isFeatureVisible } = this.props;
    return Object.entries(LAYERS)
      .filter(([, layer]) => isFeatureVisible(layer.featureName))
      .reduce((acc, [name, layer]) => ({ ...acc, [name]: layer }), {});
  }
}

LocationTab.icon = 'planning';
LocationTab.computeComparisonYears = null;
LocationTab.LAYERS = {
  tender: {
    featureName: 'viz.me.map.tenderLocations',
    component: TenderLocations,
  },
  contract: {
    featureName: 'viz.me.map.contractLocations',
    component: ContractLocations,
  },
};
LocationTab.CENTER = [14.5, 105];
LocationTab.ZOOM = 10;

LocationTab.propTypes = {
  t: PropTypes.func.isRequired,
};

export default LocationTab;
