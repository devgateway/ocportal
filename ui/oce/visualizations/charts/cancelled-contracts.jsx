import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";
import {Map} from "immutable";

class CancelledContracts extends FrontendDateFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly ?
        data.map(pluckImm('month')).map(month => this.t(`general:months:${month}`)).toArray() :
        data.map(pluckImm('year')).toArray();

    return [{
      x: dates,
      y: data.map(pluckImm('countCancelled')).toArray(),
      type: 'bar',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    const {hoverFormat} = this.props.styling.charts;
    return {
      xaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year')
      },
      yaxis: {
        title: this.t('charts:cancelledContracts:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: "   "
      }
    }
  }
}

CancelledContracts.endpoint = 'cancelledContracts';
//CancelledContracts.excelEP = 'averageNumberBidsExcelChart';
CancelledContracts.getName = t => t('charts:cancelledContracts:title');
//CancelledContracts.getFillerDatum = seed => Map(seed).set('averageNoTenderers', 0);

export default CancelledContracts;
