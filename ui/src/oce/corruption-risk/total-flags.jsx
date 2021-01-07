import React from 'react';
import PropTypes from 'prop-types';
import Chart from '../visualizations/charts/index';
import { debounce, pluckImm } from '../tools';
import backendYearFilterable from '../backend-year-filterable';
import Visualization from '../visualization';
import translatable from '../translatable';
import fmConnect from '../fm/fm';

class TotalFlagsChart extends backendYearFilterable(Chart) {
  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    const labels = data.map((datum) => this.t(`crd:corruptionType:${datum.get('type')}:name`))
      .toJS();
    return [{
      values: data.map(pluckImm('indicatorCount'))
        .toJS(),
      labels,
      textinfo: 'value',
      textposition: 'outside',
      hole: 0.8,
      type: 'pie',
      hoverlabel: {
        bgcolor: '#144361',
      },
      marker: {
        colors: ['#fac329',
          // '#289df5',
          '#3372b1'], // if you change this colors you'll have to also change it for the custom legend in ./style.scss
      },
      outsidetextfont: {
        size: 15,
        color: '#3fc529',
      },
      insidetextfont: {
        size: 15,
        color: '#3fc529',
      },
    }];
  }

  getLayout() {
    return {
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)',
      plot_bgcolor: 'rgba(0,0,0,0)',
    };
  }
}

TotalFlagsChart.endpoint = 'totalFlaggedIndicatorsByIndicatorType';

class Counter extends backendYearFilterable(Visualization) {
  render() {
    const { data } = this.props;
    if (!data) return null;
    return (
      <div className="counter">
        <div className="count text-right">
          {this.getCount()
            .toLocaleString()}
        </div>
        <div className="text text-left">
          {this.getTitle()}
        </div>
      </div>
    );
  }
}

class FlagsCounter extends Counter {
  getTitle() {
    return this.t('crd:charts:totalFlags:title');
  }

  getCount() {
    const { data } = this.props;
    return data.getIn([0, 'flaggedCount'], 0);
  }
}
FlagsCounter.endpoint = 'totalFlags';

class ContractCounter extends Counter {
  getTitle() {
    return this.t('crd:sidebar:totalContracts:title');
  }

  getCount() {
    return this.props.data;
  }
}
ContractCounter.propTypes = {
  data: PropTypes.number,
};
ContractCounter.endpoint = 'ocds/release/count';

class TotalFlags extends translatable(React.Component) {
  constructor(...args) {
    super(...args);
    this.state = {};

    this.updateSidebarWidth = debounce(() => this.setState({
      width: document.getElementById('crd-sidebar').offsetWidth,
    }));
  }

  componentDidMount() {
    this.updateSidebarWidth();
    window.addEventListener('resize', this.updateSidebarWidth);
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this.updateSidebarWidth);
  }

  render() {
    const {
      data, requestNewData, translations, filters, years, months, monthly,
    } = this.props;
    const { width } = this.state;

    if (!width) return null;
    return (
      <div className="total-flags">
        <ContractCounter
          data={data.get('contractCounter')}
          requestNewData={(_, data) => requestNewData(['contractCounter'], data)}
          translations={translations}
          filters={filters}
          years={years}
          months={months}
          monthly={monthly}
          styling={this.props.styling}
        />
        <FlagsCounter
          data={data.get('flagsCounter')}
          requestNewData={(_, data) => requestNewData(['flagsCounter'], data)}
          translations={translations}
          filters={filters}
          years={years}
          months={months}
          monthly={monthly}
          styling={this.props.styling}
        />
        <TotalFlagsChart
          data={data.get('chart')}
          requestNewData={(_, data) => requestNewData(['chart'], data)}
          translations={translations}
          width={width}
          height={200}
          margin={{
            l: 0, r: 0, t: 40, b: 20, pad: 0,
          }}
          filters={filters}
          years={years}
          months={months}
          monthly={monthly}
          styling={this.props.styling}
        />
        <div className="crd-legend">
          {/* <div className="fraud"> */}
          {/*  <span className="frc-label">{this.t('crd:corruptionType:FRAUD:name')}</span> */}
          {/* </div> */}
          <div className="rigging">
            <span className="frc-label">{this.t('crd:corruptionType:RIGGING:name')}</span>
          </div>
          <div className="collusion">
            <span className="frc-label">{this.t('crd:corruptionType:COLLUSION:name')}</span>
          </div>
        </div>
      </div>
    );
  }
}

export default fmConnect(TotalFlags, 'crd.sidebar.totalFlags');
