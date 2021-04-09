import React from 'react';
import PropTypes from 'prop-types';
import OverviewChart from '../../charts/overview';
import ProcurementMethodChart from '../../charts/procurement-method';
import ContractCount from '../../charts/contract-count';
import AmountContracted from '../../charts/amount-contracted';
import ChartTab from '../location/chart-tab';
import Tab from '../location/tab';
import LocationWrapper from '../location/popup-marker';

export class OverviewTab extends Tab {
  static getName(t) { return t('maps:tenderLocations:tabs:overview:title'); }

  render() {
    const { data, t } = this.props;
    const { count, amount } = data;
    return (
      <div>
        <p>
          <strong>{t('maps:contractLocations:tabs:overview:nrOfContracts')}</strong>
          {' '}
          {count}
        </p>
        <p>
          <strong>{t('maps:contractLocations:tabs:overview:totalContractedFundingByLocation')}</strong>
          {' '}
          {amount.toLocaleString()}
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

class ContractCountChartTab extends ChartTab {
  static getName(t) { return t('charts:contractCount:title'); }

  static getChartClass() { return 'overview'; }
}

ContractCountChartTab.Chart = ContractCount;

class AmountContractedChartTab extends ChartTab {
  static getName(t) { return t('charts:amountContracted:title'); }

  static getChartClass() { return 'overview'; }
}

AmountContractedChartTab.Chart = AmountContracted;

class ProcurementMethodTab extends ChartTab {
  static getName(t) { return t('charts:procurementMethod:title'); }
}

ProcurementMethodTab.Chart = ProcurementMethodChart;

class ContractPopupMarker extends LocationWrapper {
}

ContractPopupMarker.TABS = [OverviewTab, ContractCountChartTab, AmountContractedChartTab];

export default ContractPopupMarker;
