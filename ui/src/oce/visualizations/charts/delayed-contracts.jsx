import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";
import fmConnect from "../../fm/fm";


class DelayedContracts extends FrontendDateFilterableChart{

  getRawData(){
    return super.getData();
  }

  getData() {
    let data = super.getData();
    if (!data) return [];
    const { years } = this.props;
    const monthly = data.hasIn([0, 'month']);
    const dates = monthly ?
        data.map(pluckImm('month')).map(month => this.tMonth(month, years)).toArray() :
        data.map(pluckImm('year')).toArray();

    return [{
      x: data.map(pluckImm('countOnTime')).toArray(),
      y: dates,
      name: this.t('charts:delayedContracts:traces:countOnTime'),
      type: "bar",
      orientation: 'h',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }, {
      x: data.map(pluckImm('countDelayed')).toArray(),
      y: dates,
      name: this.t('charts:delayedContracts:traces:countDelayed'),
      type: "bar",
      orientation: 'h',
      text:  data.map(pluckImm('percentDelayed')).map(this.props.styling.charts.hoverFormatter).map(x=>x+"% delayed").toArray(),
      marker: {
        color: this.props.styling.charts.traceColors[1]
      }
    }];
  }

  getLayout() {
    const {hoverFormat} = this.props.styling.charts;
    let annotations = [];
    let data = super.getData();
    if(data){
      annotations = data.map((imm, index) => {
        let sum = imm.reduce((sum, val, key) => "year" == key || "month" == key ? sum : "percentDelayed"==key ? sum: sum +val, 0).toFixed(2);
        return {
          y: index,
          x: sum,
          xanchor: 'left',
          yanchor: 'middle',
          text: this.t('charts:delayedContracts:traces:total') + ' ' + sum,
          showarrow: false
        }
      }).toArray();
    }

    return {
      annotations,
      barmode: "stack",
      xaxis: {
        title: this.t('charts:delayedContracts:yAxisTitle'),
        hoverformat: hoverFormat
      },
      yaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: "category"
      }
    }
  }
}

DelayedContracts.endpoint = 'delayedContracts';
//BidPeriod.excelEP = 'bidTimelineExcelChart';
DelayedContracts.getName = t => t('charts:delayedContracts:title');
DelayedContracts.horizontal = true;

//BidPeriod.getFillerDatum = seed => Map(seed).set('tender', 0).set('award', 0);
//BidPeriod.getMaxField = imm => imm.get('tender', 0) + imm.get('award', 0);

export default fmConnect(DelayedContracts, 'viz.me.chart.delayedContracts');
