import Tab from './index';

class Tenders extends Tab {
  static getName(t) {
    return t('tabs:tenders:title');
  }
}

Tenders.visualizations = [];

Tenders.icon = 'eprocurement';

export default Tenders;
