import Tab from './index';
import DelayedContracts from '../visualizations/charts/delayed-contracts';
import PmcNotAuthContracts from '../visualizations/charts/pmc-not-auth-contracts';

class Implementation extends Tab {
  static getName(t) {
    return t('tabs:implementation:title');
  }
}

Implementation.icon = 'efficiency';
Implementation.visualizations = [DelayedContracts, PmcNotAuthContracts];

export default Implementation;
