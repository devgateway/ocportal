import FrontendDateFilterableChart from './frontend-date-filterable';
import { pluckImm } from '../../tools';
import PropTypes from 'prop-types';
import { tMonth } from '../../translatable';

class AmountContracted extends FrontendDateFilterableChart {
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
      y: data.map(pluckImm('amount')).toArray(),
      type: 'bar',
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
        title: t('charts:amountContracted:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: '   ',
      },
    };
  }
}

AmountContracted.endpoint = 'amountContractedByYear';
AmountContracted.excelEP = 'amountContractedByYearExcelChart';
AmountContracted.getName = (t) => t('charts:amountContracted:title');

AmountContracted.propTypes = {
  t: PropTypes.func.isRequired,
};

export default AmountContracted;
