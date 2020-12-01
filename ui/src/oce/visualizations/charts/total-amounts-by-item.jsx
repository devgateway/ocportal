import PieChart from './pie-chart';

class TotalAmountsByItem extends PieChart {

  hoverTemplate() {
    return '%{customdata} %{label} Items Procured<br>%{percent} of all items procured are <br>%{label}<extra></extra>';
  }

  static getName(t){return t('charts:amountsByItem:title')}
}

TotalAmountsByItem.endpoint = 'totalAmountByItem';
TotalAmountsByItem.LABEL_FIELD = 'description';
TotalAmountsByItem.VALUE_FIELD = 'totalAmount';

export default TotalAmountsByItem;
