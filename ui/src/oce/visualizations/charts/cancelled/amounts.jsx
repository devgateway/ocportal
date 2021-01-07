import FrontendDateFilterableChart from '../frontend-date-filterable';

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

    const { years } = this.props;
    data.forEach((datum) => {
      const date = datum.has('month')
        ? this.tMonth(datum.get('month'), years)
        : datum.get('year');

      const count = datum.get('totalCancelledTendersAmount');
      trace.x.push(date);
      trace.y.push(count);
      if (hoverFormatter) trace.text.push(hoverFormatter(count));
    });

    return [trace];
  }

  getLayout() {
    return {
      xaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: 'category',
      },
      yaxis: {
        title: this.t('charts:cancelledAmounts:yAxisName'),
        tickprefix: '   ',
      },
    };
  }
}

CancelledFunding.endpoint = 'totalCancelledTendersByYear';
CancelledFunding.excelEP = 'cancelledFundingExcelChart';

export default CancelledFunding;
