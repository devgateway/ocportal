import Tab from './index';
import BidPeriod from '../visualizations/charts/bid-period';
import NrCancelled from '../visualizations/charts/nr-cancelled';
import TotalAmountsByItem from '../visualizations/charts/total-amounts-by-item';
import BidsByItem from '../visualizations/charts/bids-by-item';

class Efficiency extends Tab {
  static getName(t) {
    return t('tabs:efficiency:title');
  }
}

Efficiency.icon = 'efficiency';
Efficiency.visualizations = [BidPeriod, TotalAmountsByItem, BidsByItem, NrCancelled];

export default Efficiency;
