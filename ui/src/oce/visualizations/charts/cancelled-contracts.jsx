import { Map } from 'immutable';
import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';

class CancelledContracts extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];
    const { years } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month')).map((month) => this.tMonth(month, years)).toArray()
      : data.map(pluckImm('year')).toArray();

    return [{
      x: dates,
      y: data.map(pluckImm('countCancelled')).toArray(),
      type: 'bar',
      marker: {
        color: this.props.styling.charts.traceColors[0],
      },
    }];
  }

  getLayout() {
    const { hoverFormat } = this.props.styling.charts;
    return {
      xaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: this.t('charts:cancelledContracts:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

CancelledContracts.endpoint = 'cancelledContracts';
// CancelledContracts.excelEP = 'averageNumberBidsExcelChart';
CancelledContracts.getName = (t) => t('charts:cancelledContracts:title');
// CancelledContracts.getFillerDatum = seed => Map(seed).set('averageNoTenderers', 0);

export default fmConnect(CancelledContracts, 'viz.me.chart.cancelledContracts');
