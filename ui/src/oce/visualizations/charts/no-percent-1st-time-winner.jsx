import FrontendDateFilterableChart from './frontend-date-filterable';
import { asPercent, pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';
import { tMonth } from '../../translatable';

class NoPercent1StTimeWinner extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];
    const { years, t } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month')).map((month) => tMonth(t, month, years)).filter(Boolean).toArray()
      : data.map(pluckImm('year')).filter(Boolean).toArray();

    if (dates.length === 0) return [];

    return [{
      x: dates,
      y: data.map(pluckImm('firstTimeWinnerAwards')).toArray(),
      hovertext: data.map((x) => t('charts:noPercent1stTimeWinner:hover')
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
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:noPercent1stTimeWinner:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

NoPercent1StTimeWinner.endpoint = 'numberPercentFirstTimeWinners';
NoPercent1StTimeWinner.getName = (t) => t('charts:noPercent1stTimeWinner:title');
// NoPercent1StTimeWinner.getFillerDatum = seed => Map(seed).set('averageNoTenderers', 0);

NoPercent1StTimeWinner.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(NoPercent1StTimeWinner, 'viz.me.chart.noPercent1StTimeWinner');
