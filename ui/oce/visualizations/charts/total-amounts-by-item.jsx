import PieChart from './pie-chart';

class TotalAmountsByItem extends PieChart {
  static getName(t){return t('charts:amountsByItem:title')}
}

TotalAmountsByItem.endpoint = 'totalAmountByItem';
TotalAmountsByItem.LABEL_FIELD = 'description';
TotalAmountsByItem.VALUE_FIELD = 'totalAmount';

export default TotalAmountsByItem;
