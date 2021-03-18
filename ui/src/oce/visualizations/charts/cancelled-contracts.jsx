import PropTypes from 'prop-types';
import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';
import { tMonth } from '../../translatable';

class CancelledContracts extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];
    const { years, t } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly
      ? data.map(pluckImm('month')).map((month) => tMonth(t, month, years)).toArray()
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
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:cancelledContracts:yAxisTitle'),
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

CancelledContracts.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(CancelledContracts, 'viz.me.chart.cancelledContracts');
