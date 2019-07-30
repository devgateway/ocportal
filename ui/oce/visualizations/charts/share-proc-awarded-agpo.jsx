import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";

class ShareProcAwardedAgpo extends FrontendDateFilterableChart{
  static getName(t){return t('charts:percentWithTenders:title')}

  getData(){
    let data = super.getData();
    console.log(data);

    return [{
      x: data.map(pluckImm('_id')).toArray(),
      y: data.map(pluckImm('value')).toArray(),
      type: 'pie',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title:  this.t('charts:percentWithTenders:xAxisTitle')
      },
      yaxis: {
        title: this.t('charts:percentWithTenders:yAxisTitle')
      }
    }
  }
}

ShareProcAwardedAgpo.endpoint = 'shareProcurementsAwardedAgpo';
ShareProcAwardedAgpo.excelEP = 'tendersWithLinkedProcurementPlanExcelChart';

export default ShareProcAwardedAgpo;
