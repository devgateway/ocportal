import CatChart from "./cat-chart";

class AvgTenderersByBuyer extends CatChart{
  static getName(t){return t('charts:avgTenderersByBuyer:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:avgTenderersByBuyer:xAxisTitle'),
        type: "category",
        tickangle: 20,
        automargin: true
      },
      yaxis: {
        title: this.t('charts:avgTenderersByBuyer:yAxisTitle'),
        tickprefix: "   "
      }
    }
  }
}

AvgTenderersByBuyer.endpoint = 'averageNumberOfTenderersPerBuyer';
AvgTenderersByBuyer.CAT_NAME_FIELD = "_id";
AvgTenderersByBuyer.CAT_VALUE_FIELD = "numberOfTenderers";

export default AvgTenderersByBuyer;
