import Tab from './index';
import DelayedContracts from '../visualizations/charts/delayed-contracts';
import PmcNotAuthContracts from '../visualizations/charts/pmc-not-auth-contracts';
import CancelledContracts from '../visualizations/charts/cancelled-contracts';

class Implementation extends Tab {
  static getName(t) {
    return t('tabs:implementation:title');
  }
}

Implementation.icon = 'efficiency';
Implementation.visualizations = [DelayedContracts, PmcNotAuthContracts, CancelledContracts];

export default Implementation;
