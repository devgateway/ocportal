import { Map } from 'immutable';
import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class AvgNrBids extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];

    const { t } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month')).map((month) => t(`general:months:${month}`)).toArray()
      : data.map(pluckImm('year')).toArray();

    return [{
      x: dates,
      y: data.map(pluckImm('averageNoTenderers')).toArray(),
      type: 'bar',
      marker: {
        color: this.props.styling.charts.traceColors[0],
      },
    }];
  }

  getLayout() {
    const { hoverFormat } = this.props.styling.charts;
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:avgNrBids:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

AvgNrBids.endpoint = 'averageNumberOfTenderersYearly';
AvgNrBids.excelEP = 'averageNumberBidsExcelChart';
AvgNrBids.getName = (t) => t('charts:avgNrBids:title');
AvgNrBids.getFillerDatum = (seed) => Map(seed).set('averageNoTenderers', 0);

AvgNrBids.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(AvgNrBids, 'viz.me.chart.avgNrBids');
