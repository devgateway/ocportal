import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";


class PmcNotAuthContracts extends FrontendDateFilterableChart{

  getRawData(){
    return super.getData();
  }

  getData() {
    let data = super.getData();
    if (!data) return [];
    const monthly = data.hasIn([0, 'month']);
    const dates = monthly ?
        data.map(pluckImm('month')).map(month => this.t(`general:months:${month}`)).toArray() :
        data.map(pluckImm('year')).toArray();

    return [{
      x: data.map(pluckImm('countAuthorized')).toArray(),
      y: dates,
      name: this.t('charts:pmcNotAuthContracts:traces:countAuthorized'),
      type: "bar",
      orientation: 'h',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }, {
      x: data.map(pluckImm('countNotAuthorized')).toArray(),
      y: dates,
      name: this.t('charts:pmcNotAuthContracts:traces:countNotAuthorized'),
      type: "bar",
      orientation: 'h',
      text:  data.map(pluckImm('percentNotAuthorized')).map(this.props.styling.charts.hoverFormatter).map(x=>x+"% not authorized").toArray(),
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
        let sum = imm.reduce((sum, val, key) => "year" == key || "month" == key ? sum : sum +val, 0).toFixed(2);
        return {
          y: index,
          x: sum,
          xanchor: 'left',
          yanchor: 'middle',
          text: this.t('charts:pmcNotAuthContracts:traces:total') + ' ' + sum,
          showarrow: false
        }
      }).toArray();
    }

    return {
      annotations,
      barmode: "stack",
      xaxis: {
        title: this.t('charts:pmcNotAuthContracts:yAxisTitle'),
        hoverformat: hoverFormat
      },
      yaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: "category"
      }
    }
  }
}

PmcNotAuthContracts.endpoint = 'pmcNotAuthContracts';
//BidPeriod.excelEP = 'bidTimelineExcelChart';
PmcNotAuthContracts.getName = t => t('charts:pmcNotAuthContracts:title');
PmcNotAuthContracts.horizontal = true;

//BidPeriod.getFillerDatum = seed => Map(seed).set('tender', 0).set('award', 0);
//BidPeriod.getMaxField = imm => imm.get('tender', 0) + imm.get('award', 0);

export default PmcNotAuthContracts;
