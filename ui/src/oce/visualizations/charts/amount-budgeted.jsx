import FrontendDateFilterableChart from "./frontend-date-filterable";
import {pluckImm} from "../../tools";

class AmountBudgeted extends FrontendDateFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    const { years } = this.props;

    const monthly = data.hasIn([0, 'month']);
    const dates = monthly ?
        data.map(pluckImm('month')).map(month => this.tMonth(month, years)).toArray() :
        data.map(pluckImm('year')).toArray();

    return [{
      x: dates,
      y: data.map(pluckImm('amount')).toArray(),
      type: 'bar',
      fill: 'tonexty',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    const {hoverFormat} = this.props.styling.charts;
    return {
      xaxis: {
        title: this.props.monthly ? this.t('general:month') : this.t('general:year'),
        type: 'category'
      },
      yaxis: {
        title: this.t('charts:amountBudgeted:yAxisTitle'),
        hoverformat: hoverFormat,
        tickprefix: "   "
      }
    }
  }
}

AmountBudgeted.endpoint = 'amountBudgetedByYear';
AmountBudgeted.excelEP = 'amountBudgetedByYearExcelChart';
AmountBudgeted.getName = t => t('charts:amountBudgeted:title');
//ProjectCount.getMaxField = pluckImm('totalTendersUsingEbid');

export default AmountBudgeted;
