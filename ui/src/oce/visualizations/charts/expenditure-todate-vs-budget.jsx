import Chart from './index';
import fmConnect from '../../fm/fm';
import PropTypes from 'prop-types';
import { pluckImm } from '../../tools';

class ExpenditureTodateVsBudget extends Chart {
  static getName(t) { return t('charts:expenditureToDateVsBudget:title'); }

  getData() {
    const data = super.getData();
    if (!data) return [];
    const years = data.map(pluckImm('fiscalYear')).toArray();
    const commonOpts = {
      y: years,
      type: 'bar',
      orientation: 'h',
      hoverlabel: {
        namelength: -1,
      },
    };
    const { t } = this.props;
    return [{
      ...commonOpts,
      x: data.map(pluckImm('expensedAmount')).toArray(),
      name: t('charts:expenditureToDateVsBudget:cat:contracted'),
      marker: {
        color: this.props.styling.charts.traceColors[0],
      },
    }, {
      ...commonOpts,
      x: data.map(pluckImm('unabsorbedAmount')).toArray(),
      name: t('charts:expenditureToDateVsBudget:cat:unabsorbed'),
      hovertext: t('charts:expenditureToDateVsBudget:cat:unabsorbedDesc'),
      marker: {
        color: this.props.styling.charts.traceColors[2],
      },
    }, {
      ...commonOpts,
      x: data.map(pluckImm('overspentAmount')).toArray(),
      name: t('charts:expenditureToDateVsBudget:cat:overspent'),
      hovertext: t('charts:expenditureToDateVsBudget:cat:overspentDesc'),
      marker: {
        color: this.props.styling.charts.traceColors[1],
      },
    }];
  }

  getLayout() {
    const { hoverFormat } = this.props.styling.charts;
    const { t } = this.props;
    return {
      barmode: 'stack',
      hovermode: 'closest',
      xaxis: {
        title: t('charts:expenditureToDateVsBudget:xAxisName'),
        hoverformat: hoverFormat,
      },
      yaxis: {
        title: t('charts:expenditureToDateVsBudget:yAxisName'),
        type: 'category',
      },
    };
  }
}

ExpenditureTodateVsBudget.endpoint = 'expenditureToDateVsBudget';
ExpenditureTodateVsBudget.horizontal = true;

ExpenditureTodateVsBudget.propTypes = {
  t: PropTypes.func.isRequired,
};

export default fmConnect(ExpenditureTodateVsBudget, 'viz.me.chart.expenditureToDateVsBudget');
