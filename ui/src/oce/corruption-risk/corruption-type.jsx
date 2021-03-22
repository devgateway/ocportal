import React from 'react';
import { List } from 'immutable';
import PropTypes from 'prop-types';
import { cacheFn, pluckImm } from '../tools';
import CustomPopupChart from './custom-popup-chart';
import CRDPage from './page';
import { colorLuminance, sortByField } from './tools';
import Crosstab from './crosstab';
import Visualization from '../visualization';
import frontendDateFilterable from '../visualizations/frontend-date-filterable';
import fmConnect from '../fm/fm';
import { tMonth } from '../translatable';

class IndicatorTile extends CustomPopupChart {
  getData() {
    const color = this.props.styling.charts.traceColors[2];
    let data = super.getData();
    if (!data) return [];
    const { monthly, years, t } = this.props;
    data = data.sort(sortByField(monthly ? 'month' : 'year'));

    const dates = monthly
      ? data.map((datum) => {
        const month = datum.get('month');
        return tMonth(t, month, years);
      }).toJS()
      : data.map(pluckImm('year')).toJS();

    const values = data.map(pluckImm('totalTrue')).toJS();

    // OCE-273, preventing the chart from showing only a dot
    if (dates.length === 1) {
      dates.unshift('');
      dates.push(' ');
      values.unshift(0);
      values.push(0);
    }

    return [{
      x: dates,
      y: values,
      hoverinfo: 'none',
      type: 'scatter',
      fill: 'tonexty',
      fillcolor: color,
      line: {
        color: colorLuminance(color, -0.3),
      },
    }];
  }

  getLayout() {
    const data = super.getData();
    const maxValue = data
      ? data.reduce((max, datum) => Math.max(max, datum.get('totalTrue')), 0)
      : 0;

    return {
      xaxis: {
        type: 'category',
        showgrid: false,
        showline: false,
        tickangle: -60,
      },
      yaxis: {
        tickangle: -90,
        tickmode: 'linear',
        tick0: 0,
        dtick: maxValue / 2,
      },
    };
  }

  getPopup() {
    const { monthly, t } = this.props;
    const { popup } = this.state;
    const { year } = popup;
    const data = super.getData();
    if (!data) return null;
    let datum;
    if (monthly) {
      datum = data.find((datum) => {
        const month = datum.get('month');
        return year === t(`general:months:${month}`);
      });
    } else {
      datum = data.find((datum) => datum.get('year') === year);
    }
    if (!datum) return null;
    return (
      <div className="crd-popup" style={{ top: popup.top, left: popup.left }}>
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr />
          </div>
          <div className="col-sm-8 text-right title">
            {t('crd:corruptionType:indicatorTile:procurementsFlagged')}
          </div>
          <div className="col-sm-4 text-left info">{datum.get('totalTrue')}</div>
          <div className="col-sm-8 text-right title">
            {t('crd:corruptionType:indicatorTile:eligibleProcurements')}
          </div>
          <div className="col-sm-4 text-left info">{datum.get('totalPrecondMet')}</div>
          <div className="col-sm-8 text-right title">
            {t('crd:corruptionType:indicatorTile:percentEligibleFlagged')}
          </div>
          <div className="col-sm-4 text-left info">
            {datum.get('percentTruePrecondMet').toFixed(2)}
            {' '}
            %
          </div>
          <div className="col-sm-8 text-right title">
            {t('crd:corruptionType:indicatorTile:percentProcurementsEligible')}
          </div>
          <div className="col-sm-4 text-left info">
            {datum.get('percentPrecondMet').toFixed(2)}
            {' '}
            %
          </div>
        </div>
        <div className="arrow" />
      </div>
    );
  }
}

IndicatorTile.propTypes = {
  t: PropTypes.func.isRequired,
};

function groupBy3(arr) {
  if (arr.length === 0) return [];
  if (arr.length <= 3) return [arr];
  return [arr.slice(0, 3)].concat(groupBy3(arr.slice(3)));
}

const getStatsForFlag = cacheFn((stats, flag) => stats.filter((stat) => stat.get('flag') === flag));

class AllTiles extends frontendDateFilterable(Visualization) {
  buildUrl(ep) {
    const { indicators } = this.props;
    const url = super.buildUrl(ep);
    url.addSearch('flags', indicators);
    return url;
  }

  componentDidUpdate(prevProps) {
    super.componentDidUpdate(prevProps);
    if (this.props.indicators !== prevProps.indicators) {
      this.fetch();
    }
  }

  // eslint-disable-next-line class-methods-use-this
  transform(data) {
    const flatStats = [];
    Object.entries(data).forEach(([flag, stats]) => {
      stats.forEach((stat) => {
        flatStats.push({
          ...stat,
          flag,
        });
      });
    });
    return flatStats;
  }

  render() {
    const {
      indicators, onGotoIndicator, corruptionType, t, filters, years, months, monthly, width, styling,
      indicatorTiles,
    } = this.props;

    return (
      <>
        {groupBy3(indicators).map((row) => (
          <div className="row" key={row.join()}>
            {row.map((indicator) => {
              const indicatorName = t(`crd:indicators:${indicator}:name`);
              const indicatorDescription = t(`crd:indicators:${indicator}:shortDescription`);
              const stats = getStatsForFlag(indicatorTiles, indicator);
              return (
                <div
                  className="col-sm-4 indicator-tile-container"
                  key={corruptionType + indicator}
                  onClick={() => onGotoIndicator(indicator)}
                  role="button"
                  tabIndex={0}
                >
                  <div className="border">
                    <h4>{indicatorName}</h4>
                    <p>{indicatorDescription}</p>
                    <IndicatorTile
                      indicator={indicator}
                      t={t}
                      filters={filters}
                      data={stats}
                      requestNewData={() => null}
                      margin={{
                        t: 10, r: 5, b: 40, l: 20, pad: 5,
                      }}
                      height={300}
                      years={years}
                      monthly={monthly}
                      months={months}
                      width={width / 3 - 47}
                      styling={styling}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        ))}
      </>
    );
  }
}

AllTiles.propTypes = {
  t: PropTypes.func.isRequired,
};

AllTiles.endpoint = 'flags/stats';

class CorruptionType extends CRDPage {
  constructor() {
    super();
    this.state = {
      indicatorTiles: List(),
    };
  }

  componentDidUpdate(prevProps) {
    if (this.props.corruptionType !== prevProps.corruptionType) {
      this.scrollTop();
    }
  }

  render() {
    const {
      indicators, onGotoIndicator, corruptionType, filters, years, monthly, months,
      t, width, styling, isFeatureVisible,
    } = this.props;
    const { crosstab, indicatorTiles } = this.state;
    if (!indicators || !indicators.length) return null;

    return (
      <div className="page-corruption-type">
        {isFeatureVisible('crd.flag.overview.charts')
        && (
          <>
            <h2 className="page-header">{t(`crd:corruptionType:${corruptionType}:pageTitle`)}</h2>
            <p
              className="introduction"
              dangerouslySetInnerHTML={{ __html: t(`crd:corruptionType:${corruptionType}:introduction`) }}
            />
            <AllTiles
              corruptionType={corruptionType}
              onGotoIndicator={onGotoIndicator}
              indicatorTiles={indicatorTiles}
              indicators={indicators}
              filters={filters}
              years={years}
              months={months}
              monthly={monthly}
              width={width}
              styling={styling}
              t={t}
              requestNewData={(_, data) => this.setState({ indicatorTiles: data })}
            />
          </>
        )}
        {isFeatureVisible('crd.flag.overview.crosstab')
        && (
          <section>
            <h3 className="page-header">
              {t(`crd:corruptionType:${corruptionType}:crosstabTitle`)}
            </h3>
            <p className="introduction">{t(`crd:corruptionType:${corruptionType}:crosstab`)}</p>
            <Crosstab
              styling={styling}
              filters={filters}
              years={years}
              monthly={monthly}
              months={months}
              indicators={indicators}
              data={crosstab}
              requestNewData={(_, data) => this.setState({ crosstab: data })}
              t={t}
            />
          </section>
        )}
      </div>
    );
  }
}

CorruptionType.propTypes = {
  isFeatureVisible: PropTypes.func.isRequired,
  t: PropTypes.func.isRequired,
};

export default fmConnect(CorruptionType);
