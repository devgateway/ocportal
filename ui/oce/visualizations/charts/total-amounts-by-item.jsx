import CatChart from './cat-chart';

class TotalAmountsByItem extends CatChart{
  static getName(t){return t('charts:amountsByItem:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:amountsByItem:xAxisTitle'),
        type: "category",
        tickangle: 15,
        automargin: true
      },
      yaxis: {
        title: this.t('charts:amountsByItem:yAxisTitle'),
        tickprefix: "   "
      }
    }
  }
}

TotalAmountsByItem.excelEP = '';
TotalAmountsByItem.CAT_VALUE_FIELD = 'totalTenderAmount';


TotalAmountsByItem.endpoint = 'totalAmountByItem';
TotalAmountsByItem.CAT_NAME_FIELD = "description";
TotalAmountsByItem.CAT_VALUE_FIELD = "totalAmount";

export default TotalAmountsByItem;
