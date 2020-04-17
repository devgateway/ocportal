import CatChart from "./cat-chart";

class AvgTenderersByBuyer extends CatChart{
  // orientation(): * {
  //   return "h";
  // }

  static getName(t){return t('charts:avgTenderersByBuyer:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:avgTenderersByBuyer:xAxisTitle'),
        type: "category",
        automargin: true,
        tickfont: {"size": 9},
        tickangle: 35,
      },
      yaxis: {
        title: this.t('charts:avgTenderersByBuyer:yAxisTitle'),
        tickprefix: "   ",
        automargin: true,
      }
    }
  }
}

AvgTenderersByBuyer.endpoint = 'averageNumberOfTenderersPerBuyer';
AvgTenderersByBuyer.CAT_NAME_FIELD = "_id";
AvgTenderersByBuyer.CAT_VALUE_FIELD = "numberOfTenderers";

export default AvgTenderersByBuyer;
