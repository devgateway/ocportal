import React from 'react';
import ReactDOM from 'react-dom';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { Popup } from 'react-leaflet';
import Component from '../../../pure-render-component';
import { download } from '../../../tools';
import Location from './marker';
import ChartTab, { addTenderDeliveryLocationId } from './chart-tab';
import exportMap from '../../../resources/icons/export-map.svg';
import cameraMap from '../../../resources/icons/camera-map.svg';
import './style.scss';

export default class LocationWrapper extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentTab: 0,
    };
  }

  doExcelExport() {
    const { currentTab } = this.state;
    const {
      data, filters, years, months, t,
    } = this.props;
    const CurrentTab = this.constructor.TABS[currentTab];
    download({
      ep: CurrentTab.Chart.excelEP,
      filters: addTenderDeliveryLocationId(filters, data._id),
      years,
      months,
      t,
    });
  }

  render() {
    const { currentTab } = this.state;
    const {
      data, t, filters, years, styling, monthly, months,
    } = this.props;
    const CurrentTab = this.constructor.TABS[currentTab];
    return (
      <Location {...this.props}>
        <Popup className="tender-locations-popup">
          <div>
            <header>
              {CurrentTab.prototype instanceof ChartTab
              && (
                <span className="chart-tools">
                  <a tabIndex={-1} role="button" onClick={() => this.doExcelExport()}>
                    <img
                      src={exportMap}
                      alt="Export"
                      width="16"
                      height="16"
                    />
                  </a>
                  <a
                    tabIndex={-1}
                    role="button"
                    onClick={() => ReactDOM.findDOMNode(this.currentChart).querySelector('.modebar-btn:first-child').click()}
                  >
                    <img
                      src={cameraMap}
                      alt="Screenshot"
                    />
                  </a>
                </span>
              )}
              {data.name}
            </header>
            <div className="row">
              <div className="tabs-bar col-xs-4">
                {this.constructor.TABS.map((tab, index) => (
                  <div
                    key={tab.getName(t)}
                    className={cn({ active: index === currentTab })}
                    onClick={() => this.setState({ currentTab: index })}
                    role="button"
                    tabIndex={0}
                  >
                    <span className="text-white">{tab.getName(t)}</span>
                  </div>
                ))}
              </div>
              <div className="col-xs-8">
                <CurrentTab
                  data={data}
                  t={t}
                  filters={filters}
                  years={years}
                  monthly={monthly}
                  months={months}
                  styling={styling}
                  ref={(c) => { this.currentChart = c; }}
                />
              </div>
            </div>
          </div>
        </Popup>
      </Location>
    );
  }
}

LocationWrapper.propTypes = {
  t: PropTypes.func.isRequired,
};
