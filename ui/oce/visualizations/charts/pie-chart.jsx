import Chart from "./index";
import backendYearFilterable from "../../backend-year-filterable";
import { pluckImm } from '../../tools';

class PieChart extends backendYearFilterable(Chart) {

  hoverTemplate() {
    return undefined;
  }

  getData(){
    const data = super.getData();
    if(!data || !data.count()) return [];
    const labels = data.map(pluckImm(this.constructor.LABEL_FIELD)).toJS();
    const values = data.map(pluckImm(this.constructor.VALUE_FIELD)).toJS();
    const text = [];
    for (let i = 0; i < values.length; i += 1) {
      text.push(this.props.styling.charts.hoverFormatter(values[i]));
    }
    return [{
      values: values,
      labels: labels,
      customdata: values,
      text: text,
      textposition: 'inside',
      textinfo: 'text',
      hoverinfo: 'text+percent',
      type: 'pie'
    }];
  }

  getLayout(){
    return {
      showlegend: true,
      legend: {
        font: {
          size: 9,
        }
      }
    }
  }

}


export default PieChart;
