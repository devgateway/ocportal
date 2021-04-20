import React from 'react';
import PropTypes from 'prop-types';
import CustomPopupChart from '../custom-popup-chart';
import { pluckImm } from '../../tools';
import CRDPage from '../page';
import { colorLuminance, sortByField } from '../tools';
import ProcurementsTable from './table';
import fmConnect from '../../fm/fm';
import { tMonth } from '../../translatable';

class IndividualIndicatorChart extends CustomPopupChart {
  getCustomEP() {
    const { indicator } = this.props;
    return `flags/${indicator}/stats`;
  }

  getData() {
    let data = super.getData();
    const { traceColors } = this.props.styling.charts;
    if (!data) return [];
    const { monthly, years, t } = this.props;

    data = data.sort(sortByField(monthly ? 'month' : 'year'));

    const dates = monthly
      ? data.map((datum) => {
        const month = datum.get('month');
        return tMonth(t, month, years);
      }).toJS()
      : data.map(pluckImm('year')).toJS();

    const totalTrueValues = data.map(pluckImm('totalTrue', 0)).toJS();
    const totalPrecondMetValues = data.map(pluckImm('totalPrecondMet', 0)).toJS();

    if (dates.length === 1) {
      dates.unshift('');
      dates.push(' ');
      totalTrueValues.unshift(0);
      totalTrueValues.push(0);
      totalPrecondMetValues.unshift(0);
      totalPrecondMetValues.push(0);
    }

    return [{
      x: dates,
      y: totalTrueValues,
      type: 'scatter',
      fill: 'tonexty',
      name: t('crd:individualIndicatorChart:flaggedProcurements'),
      hoverinfo: 'none',
      fillcolor: traceColors[0],
      line: {
        color: colorLuminance(traceColors[0], -0.3),
      },
    }, {
      x: dates,
      y: totalPrecondMetValues,
      type: 'scatter',
      fill: 'tonexty',
      name: t('crd:individualIndicatorChart:eligibleProcurements'),
      hoverinfo: 'none',
      fillcolor: traceColors[1],
      line: {
        color: colorLuminance(traceColors[1], -0.3),
      },
    }];
  }

  getLayout() {
    return {
      legend: {
        orientation: 'h',
        xanchor: 'right',
        yanchor: 'bottom',
        x: 1,
        y: 1,
      },
      hovermode: 'closest',
      xaxis: {
        type: 'category',
        showgrid: false,
      },
      yaxis: {},
    };
  }

  getPopupWidth() {
    const { t } = this.props;
    const label = t('crd:indicatorPage:individualIndicatorChart:popup:percentEligible');
    return label.length > 23
      ? 500
      : 350;
  }

  getPopup() {
    const { monthly, years, t } = this.props;
    const { popup } = this.state;
    const { year } = popup;
    const data = super.getData();
    if (!data) return null;
    let datum;

    if (monthly) {
      datum = data.find((datum) => {
        const month = datum.get('month');
        return year === tMonth(t, month, years);
      });
    } else {
      datum = data.find((datum) => datum.get('year') === year);
    }

    return (
      <div
        className="crd-popup"
        style={{
          top: popup.top,
          left: popup.left,
          width: this.getPopupWidth(),
        }}
      >
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr />
          </div>
          <div className="col-sm-8 text-right title">
            {t('crd:indicatorPage:individualIndicatorChart:popup:procurementsFlagged')}
          </div>
          <div className="col-sm-4 text-left info">{datum.get('totalTrue')}</div>
          <div className="col-sm-8 text-right title">
            {t('crd:indicatorPage:individualIndicatorChart:popup:eligibleProcurements')}
          </div>
          <div className="col-sm-4 text-left info">{datum.get('totalPrecondMet')}</div>
          <div className="col-sm-8 text-right title">
            {t('crd:indicatorPage:individualIndicatorChart:popup:percentOfEligibleFlagged')}
          </div>
          <div className="col-sm-4 text-left info">
            {datum.get('percentTruePrecondMet').toFixed(2)}
            {' '}
            %
          </div>
          <div className="col-sm-8 text-right title">
            {t('crd:indicatorPage:individualIndicatorChart:popup:percentEligible')}
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

IndividualIndicatorChart.propTypes = {
  t: PropTypes.func.isRequired,
};

class IndividualIndicatorPage extends CRDPage {
  constructor(...args) {
    super(...args);
    this.state = {};
  }

  render() {
    const { chart, table } = this.state;
    const {
      corruptionType, indicator, t, filters, years, monthly, months, width,
      styling, isFeatureVisible,
    } = this.props;

    return (
      <div className="page-individual-indicator">
        <h2 className="page-header">{t(`crd:indicators:${indicator}:name`)}</h2>
        <p className="definition">
          <strong>{t('crd:indicators:general:indicator')}</strong>
          &nbsp;
          {t(`crd:indicators:${indicator}:indicator`)}
        </p>
        <p className="definition">
          <strong>{t('crd:indicators:general:eligibility')}</strong>
          &nbsp;
          {t(`crd:indicators:${indicator}:eligibility`)}
        </p>
        <p className="definition">
          <strong>{t('crd:indicators:general:thresholds')}</strong>
          &nbsp;
          {t(`crd:indicators:${indicator}:thresholds`)}
        </p>
        <p className="definition">
          <strong>{t('crd:indicators:general:description')}</strong>
          &nbsp;
          {t(`crd:indicators:${indicator}:description`)}
        </p>
        {isFeatureVisible('crd.flag.indicator.chart')
        && (
          <section>
            <h3 className="page-header">
              {t('crd:indicatorPage:individualIndicatorChart:title')
                .replace('$#$', t(`crd:indicators:${indicator}:name`))}
            </h3>
            <IndividualIndicatorChart
              indicator={indicator}
              t={t}
              filters={filters}
              years={years}
              monthly={monthly}
              months={months}
              requestNewData={(_, data) => this.setState({ chart: data })}
              data={chart}
              width={width - 20}
              styling={styling}
              margin={{
                t: 0, b: 80, r: 20, pad: 40,
              }}
            />
          </section>
        )}
        {isFeatureVisible('crd.flag.indicator.procurements')
        && (
          <section className="table-section">
            <h3 className="page-header">
              {t('crd:indicatorPage:projectTable:title')
                .replace('$#$', t(`crd:indicators:${indicator}:name`))}
            </h3>
            <ProcurementsTable
              dataEP={`flags/${indicator}/releases?flagType=${corruptionType}`}
              countEP={`flags/${indicator}/count?flagType=${corruptionType}`}
              indicator={indicator}
              corruptionType={corruptionType}
              requestNewData={(_, data) => this.setState({ table: data })}
              data={table}
              t={t}
              filters={filters}
              years={years}
              monthly={monthly}
              months={months}
            />
          </section>
        )}
      </div>
    );
  }
}

IndividualIndicatorPage.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(IndividualIndicatorPage);
