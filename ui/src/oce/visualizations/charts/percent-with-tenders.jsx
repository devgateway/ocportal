import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import PropTypes from 'prop-types';
import { tMonth } from '../../translatable';

class PercentWithTenders extends FrontendDateFilterableChart {
  static getName(t) { return t('charts:percentWithTenders:title'); }

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
      y: data.map(pluckImm('percentTenders')).toArray(),
      type: 'scatter',
      fill: 'tonexty',
      marker: {
        color: this.props.styling.charts.traceColors[0],
      },
    }];
  }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:percentWithTenders:yAxisTitle'),
        hoverformat: '.2f',
        tickprefix: '   ',
      },
    };
  }
}

PercentWithTenders.endpoint = 'percentTendersWithLinkedProcurementPlan';
PercentWithTenders.excelEP = 'tendersWithLinkedProcurementPlanExcelChart';
PercentWithTenders.getMaxField = (imm) => imm.get('percentTenders', 0);

PercentWithTenders.propTypes = {
  t: PropTypes.func.isRequired,
};

export default PercentWithTenders;
