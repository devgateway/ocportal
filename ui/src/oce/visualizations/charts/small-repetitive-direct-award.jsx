import CatChart from './cat-chart';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class SmallRepetitiveDirectAwardsBuyer extends CatChart {
  static getName(t) { return t('charts:smallRepetitiveDirectAwardsByBuyer:title'); }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: t('charts:smallRepetitiveDirectAwardsByBuyer:xAxisTitle'),
        type: 'category',
        tickangle: -45,
        automargin: true,
        tickfont: { size: 9 },
      },
      yaxis: {
        title: t('charts:smallRepetitiveDirectAwardsByBuyer:yAxisTitle'),
        tickprefix: '   ',
      },
    };
  }
}

SmallRepetitiveDirectAwardsBuyer.endpoint = 'smallRepetitiveDirectAwardsByBuyer';
SmallRepetitiveDirectAwardsBuyer.CAT_NAME_FIELD = '_id';
SmallRepetitiveDirectAwardsBuyer.CAT_VALUE_FIELD = 'cnt';
SmallRepetitiveDirectAwardsBuyer.CAT_WRAP_CHARS = 40;

SmallRepetitiveDirectAwardsBuyer.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(SmallRepetitiveDirectAwardsBuyer, 'viz.me.chart.smallRepetitiveDirectAwardsBuyer');
