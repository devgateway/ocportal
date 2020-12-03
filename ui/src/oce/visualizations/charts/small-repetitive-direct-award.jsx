import CatChart from "./cat-chart";
import fmConnect from "../../fm/fm";

class SmallRepetitiveDirectAwardsBuyer extends CatChart{
  static getName(t){return t('charts:smallRepetitiveDirectAwardsByBuyer:title')}

  getLayout(){
    return {
      xaxis: {
        title: this.t('charts:smallRepetitiveDirectAwardsByBuyer:xAxisTitle'),
        type: "category",
        tickangle: 35,
        automargin: true,
        tickfont: {"size": 9},
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
SmallRepetitiveDirectAwardsBuyer.CAT_WRAP_CHARS = 40;

export default fmConnect(SmallRepetitiveDirectAwardsBuyer, 'viz.me.chart.smallRepetitiveDirectAwardsBuyer');
