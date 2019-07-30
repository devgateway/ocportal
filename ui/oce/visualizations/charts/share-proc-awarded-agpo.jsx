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
    return [{
      values: data.map(pluckImm('value')).toJS(),
      labels: labels,
      textinfo: 'value',
      type: 'pie',
      hoverlabel: {
        bgcolor: '#144361'
      },
      marker: {
        colors: ['#fac329', '#144361', '#3372b1']//if you change this colors you'll have to also change it for the custom legend in ./style.less
      },
      outsidetextfont: {
        size: 15,
        color: '#00c50f'
      },
      insidetextfont: {
        size: 15,
        color: '#00c50f'
      },
    }];
  }

  getLayout(){
    const {width} = this.props;
    return {
      showlegend: false,
      paper_bgcolor: 'rgba(0,0,0,0)'
    }
  }
}

ShareProcAwardedAgpo.getName = t => t('charts:shareProcAwardedAgpo:title');
ShareProcAwardedAgpo.endpoint = 'shareProcurementsAwardedAgpo';


export default ShareProcAwardedAgpo;
