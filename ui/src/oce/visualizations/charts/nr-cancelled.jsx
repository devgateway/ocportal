import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';
import { tMonth } from '../../translatable';

class NrCancelled extends FrontendDateFilterableChart {
  static getName(t) { return t('charts:nrCancelled:title'); }

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
      y: data.map(pluckImm('totalCancelled')).toArray(),
      type: 'scatter',
      fill: 'tonexty',
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
        title: t('charts:nrCancelled:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

NrCancelled.endpoint = 'percentTendersCancelled';
NrCancelled.excelEP = 'numberCancelledFundingExcelChart';
NrCancelled.getMaxField = (imm) => imm.get('totalCancelled');

NrCancelled.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(NrCancelled, 'viz.me.chart.nrCancelled');
