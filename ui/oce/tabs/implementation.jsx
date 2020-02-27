import Tab from './index';
import DelayedContracts from '../visualizations/charts/delayed-contracts';

class Implementation extends Tab {
  static getName(t) {
    return t('tabs:implementation:title');
  }
}

Implementation.icon = 'efficiency';
Implementation.visualizations = [DelayedContracts];

export default Implementation;
