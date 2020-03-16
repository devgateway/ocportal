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
    // const ultimateColors = [
    //   ['rgb(56, 75, 126)', 'rgb(18, 36, 37)', 'rgb(34, 53, 101)', 'rgb(36, 55, 57)', 'rgb(6, 4, 4)'],
    // ];

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
      // marker: {
      //   colors: ultimateColors[0]
      // },
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
