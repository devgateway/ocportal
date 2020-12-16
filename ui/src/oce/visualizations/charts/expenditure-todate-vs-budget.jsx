import CatChart from './cat-chart';

class ExpenditureTodateVsBudget extends CatChart {
  static getName(t){return t('charts:expenditureToDateVsBudget:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:expenditureToDateVsBudget:xAxisName'),
        type: "category"
      },
      yaxis: {
        title: this.t('charts:expenditureToDateVsBudget:yAxisName'),
        tickprefix: "   "
      }
    }
  }

}

ExpenditureTodateVsBudget.endpoint = 'expenditureToDateVsBudget';
ExpenditureTodateVsBudget.CAT_NAME_FIELD = 'type';
ExpenditureTodateVsBudget.CAT_VALUE_FIELD = 'amount';

export default ExpenditureTodateVsBudget;
