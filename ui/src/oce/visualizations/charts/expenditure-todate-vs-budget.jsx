import CatChart from './cat-chart';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class ExpenditureTodateVsBudget extends CatChart {
  static getName(t) { return t('charts:expenditureToDateVsBudget:title'); }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: t('charts:expenditureToDateVsBudget:xAxisName'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:expenditureToDateVsBudget:yAxisName'),
        tickprefix: '   ',
      },
    };
  }
}

ExpenditureTodateVsBudget.endpoint = 'expenditureToDateVsBudget';
ExpenditureTodateVsBudget.CAT_NAME_FIELD = 'type';
ExpenditureTodateVsBudget.CAT_VALUE_FIELD = 'amount';

ExpenditureTodateVsBudget.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(ExpenditureTodateVsBudget, 'viz.me.chart.expenditureToDateVsBudget');
