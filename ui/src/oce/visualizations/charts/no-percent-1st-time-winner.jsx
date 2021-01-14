import FrontendDateFilterableChart from './frontend-date-filterable';
import { asPercent, pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';

class NoPercent1StTimeWinner extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];
    const { years } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month')).map((month) => this.tMonth(month, years)).filter(Boolean).toArray()
      : data.map(pluckImm('year')).filter(Boolean).toArray();

    if (dates.length === 0) return [];

    return [{
      x: dates,
      y: data.map(pluckImm('firstTimeWinnerAwards')).toArray(),
      hovertext: data.map((x) => this.t('charts:noPercent1stTimeWinner:hover')
        .replace('[#]', asPercent(x.get('percentFirstTimeWinner')))
        .replace('[#]', x.get('countAwards'))).toArray(),
      hoverinfo: 'text',
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
        title: this.t('charts:noPercent1stTimeWinner:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

NoPercent1StTimeWinner.endpoint = 'numberPercentFirstTimeWinners';
NoPercent1StTimeWinner.getName = (t) => t('charts:noPercent1stTimeWinner:title');
// NoPercent1StTimeWinner.getFillerDatum = seed => Map(seed).set('averageNoTenderers', 0);

export default fmConnect(NoPercent1StTimeWinner, 'viz.me.chart.noPercent1StTimeWinner');
