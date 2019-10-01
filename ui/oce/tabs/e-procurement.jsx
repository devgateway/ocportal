import Tab from './index';
import PercentWithTenders from '../visualizations/charts/percent-with-tenders.jsx';

class EProcurement extends Tab {
  static getName(t) {
    return t('tabs:eProcurement:title');
  }
}

EProcurement.icon = 'eprocurement';
//EProcurement.visualizations = [PercentEBid, NrEbid, PercentWithTenders];
EProcurement.visualizations = [PercentWithTenders];

export default EProcurement;
