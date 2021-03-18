import CatChart from './cat-chart';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';

class ProcurementMethod extends CatChart {
  static getName(t) { return t('charts:procurementMethod:title'); }

  static getCatName(datum, t) {
    return datum.get(this.CAT_NAME_FIELD) || t('charts:procurementMethod:unspecified');
  }

  getLayout() {
    const { t } = this.props;
    return {
      xaxis: {
        title: t('charts:procurementMethod:xAxisName'),
        type: 'category',
      },
      yaxis: {
        title: t('charts:procurementMethod:yAxisName'),
        tickprefix: '   ',
      },
    };
  }

  getDecoratedLayout() {
    if (window.innerWidth > 1600) return super.getDecoratedLayout();
    const layout = JSON.parse(JSON.stringify(super.getDecoratedLayout()));
    layout.margin.b = 150;
    layout.margin.r = 100;
    return layout;
  }
}

ProcurementMethod.endpoint = 'tenderPriceByProcurementMethod';
ProcurementMethod.excelEP = 'procurementMethodExcelChart';
ProcurementMethod.CAT_NAME_FIELD = 'procurementMethod';
ProcurementMethod.CAT_VALUE_FIELD = 'totalTenderAmount';

ProcurementMethod.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(ProcurementMethod, 'viz.me.chart.procurementMethod');
