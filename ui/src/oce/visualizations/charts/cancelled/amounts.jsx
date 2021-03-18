import FrontendDateFilterableChart from '../frontend-date-filterable';
import PropTypes from 'prop-types';
import { tMonth } from '../../../translatable';

class CancelledFunding extends FrontendDateFilterableChart {
  getData() {
    const data = super.getData();
    if (!data) return [];
    const { traceColors, hoverFormatter } = this.props.styling.charts;
    const trace = {
      x: [],
      y: [],
      type: 'scatter',
      fill: 'tonexty',
      marker: {
        color: traceColors[0],
      },
    };

    if (hoverFormatter) {
      trace.text = [];
      trace.hoverinfo = 'text';
    }

    const { years, t } = this.props;
    data.forEach((datum) => {
      const date = datum.has('month')
        ? tMonth(t, datum.get('month'), years)
        : datum.get('year');

      const count = datum.get('totalCancelledTendersAmount');
      trace.x.push(date);
      trace.y.push(count);
      if (hoverFormatter) trace.text.push(hoverFormatter(count));
    });

    return [trace];
  }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: this.props.monthly ? t('general:month') : t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:cancelledAmounts:yAxisName'),
        tickprefix: '   ',
      },
    };
  }
}

CancelledFunding.propTypes = {
  t: PropTypes.func.isRequired,
};

CancelledFunding.endpoint = 'totalCancelledTendersByYear';
CancelledFunding.excelEP = 'cancelledFundingExcelChart';

export default CancelledFunding;
