import Plotly from "plotly.js/lib/core";
import backendYearFilterable from "../../backend-year-filterable";
import Chart from './index';
import { pluckImm } from '../../tools';

Plotly.register([
  require('plotly.js/lib/pie')
]);


class ShareProcAwardedAgpo extends backendYearFilterable(Chart){

  getData(){
    const data = super.getData();
    if(!data || !data.count()) return [];
    const labels = data.map(pluckImm('_id')).toJS();
    const values = data.map(pluckImm('value')).toJS();
    const text = [];
    for (let i = 0; i < values.length; i += 1) {
      text.push(this.props.styling.charts.hoverFormatter(values[i]));
    }
    return [{
      values: values,
      labels: labels,
      text: text,
      textposition: 'inside',
      hoverinfo: 'text+percent',
      type: 'pie'
      //,
      // marker: {
      //   colors: ['#dca402', '#144361', '#3372b1']//if you change this colors you'll have to also change it for the custom legend in ./style.less
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

  getLayout(){
    return {
      showlegend: true
    }
  }
}

ShareProcAwardedAgpo.getName = t => t('charts:shareProcAwardedAgpo:title');
ShareProcAwardedAgpo.endpoint = 'shareProcurementsAwardedAgpo';


export default ShareProcAwardedAgpo;
