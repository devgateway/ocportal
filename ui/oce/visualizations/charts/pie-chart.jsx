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
    const hovertext = [];
    const colors = ['rgb(9, 66, 115)', 'rgb(26, 95, 155,)', 'rgb(24, 123, 209)', 'rgb(64, 166, 255)'
      ,'rgb(54, 253, 255)','rgb(55, 255, 140)', 'rgb(30, 213, 31)', 'rgb(31, 178, 32)','rgb(0, 130, 0)',
  'rgb(2, 94, 2)','rgb(244, 191, 0)','rgb(245, 147, 0)', 'rgb(223, 109, 0)','rgb(223, 67, 0)','rgb(221, 68, 0)'];
    for (let i = 0; i < values.length; i += 1) {
      text.push(this.props.styling.charts.hoverFormatter(values[i]));
      hovertext.push(this.props.styling.charts.hoverFormatter(values[i])+' '+labels[i]);
    }
    return [{
      values: values,
      labels: labels,
      customdata: values,
      text: text,
      hovertext: hovertext,
      textposition: 'inside',
      textinfo: 'text',
      hoverinfo: 'text+percent',
      marker: {
        colors: colors
      },
      hovertemplate: this.hoverTemplate(),
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
