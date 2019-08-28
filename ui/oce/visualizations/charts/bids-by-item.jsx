import CatChart from "./cat-chart";

class BidsByItem extends CatChart{
  static getName(t){return t('charts:bidsByItem:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:bidsByItem:xAxisTitle'),
        type: "category",
        tickangle: 15,
        automargin: true
      },
      yaxis: {
        title: this.t('charts:bidsByItem:yAxisTitle'),
        tickprefix: "   "
      }
    }
  }
}

BidsByItem.endpoint = 'tendersByItemClassification';
BidsByItem.excelEP = 'tendersByItemExcelChart';
BidsByItem.CAT_NAME_FIELD = "description";
BidsByItem.CAT_VALUE_FIELD = "tenderCount";

export default BidsByItem;
