import backendYearFilterable from '../../backend-year-filterable';
import Chart from './index';
import { pluckImm } from '../../tools';
import fmConnect from '../../fm/fm';

const BAR_CHART_THRESHOLD = 1 / 1000000;

class ShareProcAwardedAgpo extends backendYearFilterable(Chart) {
  getData() {
    const data = super.getData();
    if (!data || !data.count()) return [];
    const labels = data.map(pluckImm('_id')).toJS();
    const values = data.map(pluckImm('value')).toJS();
    const text = [];
    for (let i = 0; i < values.length; i += 1) {
      text.push(this.props.styling.charts.hoverFormatter(values[i]));
    }
    const smallestPie = Math.min(...values.filter((v) => v > 0)) / values.reduce((acc, val) => acc + val, 0);
    if (smallestPie < BAR_CHART_THRESHOLD) {
      const { traceColors } = this.props.styling.charts;
      return [{
        showlegend: false,
        x: labels,
        y: values,
        type: 'bar',
        marker: {
          color: traceColors[0],
        },
      }];
    }
    return [{
      values,
      labels,
      text,
      textposition: 'inside',
      hoverinfo: 'text+percent',
      type: 'pie',
      // ,
      // marker: {
      //   colors: ['#dca402', '#144361', '#3372b1']//if you change this colors you'll have to also change it for the custom legend in ./style.scss
      // },
      // outsidetextfont: {
      //   size: 15,
      //   color: '#00c50f'
      // },
      // insidetextfont: {
      //   size: 15,
      //   color: '#00c50f'
      // },
    }];
  }

  getLayout() {
    return {
      showlegend: true,
      xaxis: {
        title: 'Category',
        type: 'category',
        automargin: true,
        tickfont: { size: 9 },
        tickangle: -45,
      },
      yaxis: {
        title: 'Amount',
        tickprefix: '   ',
        automargin: true,
      },
    };
  }
}

ShareProcAwardedAgpo.getName = (t) => t('charts:shareProcAwardedAgpo:title');
ShareProcAwardedAgpo.endpoint = 'shareProcurementsAwardedAgpo';

export default fmConnect(ShareProcAwardedAgpo, 'viz.me.chart.shareProcAwardedAgpo');
