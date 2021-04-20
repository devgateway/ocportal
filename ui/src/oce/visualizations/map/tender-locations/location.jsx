import React from 'react';
import PropTypes from 'prop-types';
import OverviewChart from '../../charts/overview';
import CostEffectiveness from '../../charts/cost-effectiveness';
import ProcurementMethodChart from '../../charts/procurement-method';
import ProjectCount from '../../charts/project-count';
import AmountBudgeted from '../../charts/amount-budgeted';
import ChartTab from '../location/chart-tab';
import Tab from '../location/tab';
import LocationWrapper from '../location/popup-marker';

export class OverviewTab extends Tab {
  static getName(t) { return t('maps:tenderLocations:tabs:overview:title'); }

  render() {
    const { data, t } = this.props;
    const { count, totalProjectsAmount } = data;
    return (
      <div>
        <p>
          <strong>{t('maps:tenderLocations:tabs:overview:nrOfProjects')}</strong>
          {' '}
          {count}
        </p>
        <p>
          <strong>{t('maps:tenderLocations:tabs:overview:totalFundingByLocation')}</strong>
          {' '}
          {totalProjectsAmount.toLocaleString()}
        </p>
      </div>
    );
  }
}

OverviewTab.propTypes = {
  t: PropTypes.func.isRequired,
};

class OverviewChartTab extends ChartTab {
  static getName(t) { return t('charts:overview:title'); }

  static getChartClass() { return 'overview'; }
}

OverviewChartTab.Chart = OverviewChart;

class CostEffectivenessTab extends ChartTab {
  static getName(t) { return t('charts:costEffectiveness:title'); }
}

CostEffectivenessTab.Chart = CostEffectiveness;

class ProjectCountChartTab extends ChartTab {
  static getName(t) { return t('charts:projectCount:title'); }

  static getChartClass() { return 'overview'; }
}

ProjectCountChartTab.Chart = ProjectCount;

class AmountBudgetedChartTab extends ChartTab {
  static getName(t) { return t('charts:amountBudgeted:title'); }

  static getChartClass() { return 'overview'; }
}

AmountBudgetedChartTab.Chart = AmountBudgeted;

class ProcurementMethodTab extends ChartTab {
  static getName(t) { return t('charts:procurementMethod:title'); }
}

ProcurementMethodTab.Chart = ProcurementMethodChart;

class TenderPopupMarker extends LocationWrapper {
}

TenderPopupMarker.TABS = [OverviewTab, ProjectCountChartTab, AmountBudgetedChartTab];

export default TenderPopupMarker;
