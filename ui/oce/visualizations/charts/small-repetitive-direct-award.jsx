import CatChart from "./cat-chart";

class SmallRepetitiveDirectAwardsBuyer extends CatChart{
  static getName(t){return t('charts:smallRepetitiveDirectAwardsByBuyer:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:smallRepetitiveDirectAwardsByBuyer:xAxisTitle'),
        type: "category"
      },
      yaxis: {
        title: this.t('charts:smallRepetitiveDirectAwardsByBuyer:yAxisTitle'),
        tickprefix: "   "
      }
    }
  }
}

SmallRepetitiveDirectAwardsBuyer.endpoint = 'smallRepetitiveDirectAwardsByBuyer';
SmallRepetitiveDirectAwardsBuyer.CAT_NAME_FIELD = "_id";
SmallRepetitiveDirectAwardsBuyer.CAT_VALUE_FIELD = "cnt";

export default SmallRepetitiveDirectAwardsBuyer;
