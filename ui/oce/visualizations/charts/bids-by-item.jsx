import PieChart from './pie-chart';

class BidsByItem extends PieChart{
  static getName(t){return t('charts:bidsByItem:title')}
}

BidsByItem.endpoint = 'tendersByItemClassification';
BidsByItem.LABEL_FIELD = 'description';
BidsByItem.VALUE_FIELD = 'tenderCount';

export default BidsByItem;
