import Tab from './index';

class EProcurement extends Tab {
  static getName(t) {
    return t('tabs:eProcurement:title');
  }
}

EProcurement.icon = 'eprocurement';
// EProcurement.visualizations = [PercentEBid, NrEbid, PercentWithTenders];
EProcurement.visualizations = [];

export default EProcurement;
