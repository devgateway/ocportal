import CatChart from './cat-chart';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class AvgTenderersByBuyer extends CatChart {
  // orientation(): * {
  //   return "h";
  // }

  static getName(t) { return t('charts:avgTenderersByBuyer:title'); }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: t('charts:avgTenderersByBuyer:xAxisTitle'),
        type: 'category',
        automargin: true,
        tickfont: { size: 9 },
        tickangle: -45,
      },
      yaxis: {
        title: t('charts:avgTenderersByBuyer:yAxisTitle'),
        tickprefix: '   ',
        automargin: true,
      },
    };
  }
}

AvgTenderersByBuyer.endpoint = 'averageNumberOfTenderersPerBuyer';
AvgTenderersByBuyer.CAT_NAME_FIELD = '_id';
AvgTenderersByBuyer.CAT_WRAP_CHARS = 40;
AvgTenderersByBuyer.CAT_VALUE_FIELD = 'numberOfTenderers';

AvgTenderersByBuyer.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(AvgTenderersByBuyer, 'viz.me.chart.avgTenderersByBuyer');
