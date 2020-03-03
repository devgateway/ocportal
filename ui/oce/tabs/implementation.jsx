import Tab from './index';
import DelayedContracts from '../visualizations/charts/delayed-contracts';
import PmcNotAuthContracts from '../visualizations/charts/pmc-not-auth-contracts';
import CancelledContracts from '../visualizations/charts/cancelled-contracts';
import TopSuppliersInspectionReportNoPay
  from '../visualizations/tables/top-suppliers-inspection-report-nopay';
import TopSuppliersDelayedContracts from '../visualizations/tables/top-suppliers-delayed-contracts';

class Implementation extends Tab {
  static getName(t) {
    return t('tabs:implementation:title');
  }
}

Implementation.icon = 'efficiency';
Implementation.visualizations = [DelayedContracts, PmcNotAuthContracts, CancelledContracts,
  TopSuppliersInspectionReportNoPay, TopSuppliersDelayedContracts];

export default Implementation;
