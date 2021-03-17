import Chart from './index';
import fmConnect from '../../fm/fm';
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
    return [{
      ...commonOpts,
      x: data.map(pluckImm('expensedAmount')).toArray(),
      name: this.t('charts:expenditureToDateVsBudget:cat:contracted'),
      marker: {
        color: this.props.styling.charts.traceColors[0],
      },
    }, {
      ...commonOpts,
      x: data.map(pluckImm('unabsorbedAmount')).toArray(),
      name: this.t('charts:expenditureToDateVsBudget:cat:unabsorbed'),
      hovertext: this.t('charts:expenditureToDateVsBudget:cat:unabsorbedDesc'),
      marker: {
        color: this.props.styling.charts.traceColors[2],
      },
    }, {
      ...commonOpts,
      x: data.map(pluckImm('overspentAmount')).toArray(),
      name: this.t('charts:expenditureToDateVsBudget:cat:overspent'),
      hovertext: this.t('charts:expenditureToDateVsBudget:cat:overspentDesc'),
      marker: {
        color: this.props.styling.charts.traceColors[1],
      },
    }];
  }

  getLayout() {
    const { hoverFormat } = this.props.styling.charts;
    return {
      barmode: 'stack',
      hovermode: 'closest',
      xaxis: {
        title: this.t('charts:expenditureToDateVsBudget:xAxisName'),
        hoverformat: hoverFormat,
      },
      yaxis: {
        title: this.t('charts:expenditureToDateVsBudget:yAxisName'),
        type: 'category',
      },
    };
  }
}

ExpenditureTodateVsBudget.endpoint = 'expenditureToDateVsBudget';
ExpenditureTodateVsBudget.horizontal = true;

export default fmConnect(ExpenditureTodateVsBudget, 'viz.me.chart.expenditureToDateVsBudget');
