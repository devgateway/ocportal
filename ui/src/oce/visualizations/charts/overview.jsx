import FrontendDateFilterableChart from './frontend-date-filterable';
import { yearlyResponse2obj, monthlyResponse2obj, pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';
import { tMonth } from '../../translatable';

class OverviewChart extends FrontendDateFilterableChart {
  transform([tendersResponse, awardsResponse]) {
    const monthly = tendersResponse && tendersResponse[0] && tendersResponse[0].month;
    const response2obj = monthly ? monthlyResponse2obj : yearlyResponse2obj;
    const tenders = response2obj('count', tendersResponse);
    const awards = response2obj('count', awardsResponse);
    const dateKey = monthly ? 'month' : 'year';
    return Object.keys(tenders)
      .map((date) => ({
        [dateKey]: date,
        tender: tenders[date],
        award: awards[date],
      }));
  }

  getRawData() {
    return super.getData();
  }

  getData() {
    const data = super.getData();
    if (!data) return [];
    const { t } = this.props;
    const LINES = {
      award: t('charts:overview:traces:award'),
      tender: t('charts:overview:traces:tender'),
    };

    const { years } = this.props;
    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month'))
        .map((month) => tMonth(t, month, years))
        .toArray()
      : data.map(pluckImm('year'))
        .toArray();

    return Object.keys(LINES)
      .map((key, index) => ({
        x: dates,
        y: data.map(pluckImm(key))
          .toArray(),
        type: 'scatter',
        name: LINES[key],
        marker: {
          color: this.props.styling.charts.traceColors[index],
        },
      }));
  }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        titlefont: {
          color: '#223a49',
        },
        type: 'category',
      },
      yaxis: {
        title: {
          text: t('charts:overview:yAxisName'),
          font: {
            color: '#223a49',
          },
        },
        exponentformat: 'none',
        tickprefix: '   ',
      },
    };
  }
}

OverviewChart.endpoints = ['countTendersByYear', 'countAwardsByYear'];
OverviewChart.excelEP = 'procurementActivityExcelChart';

OverviewChart.getName = (t) => t('charts:overview:title');

OverviewChart.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(OverviewChart, 'viz.me.chart.overview');
