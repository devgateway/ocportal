import PieChart from './pie-chart';

class AvgTenderersByBuyer extends PieChart {
  static getName(t){return t('charts:avgTenderersByBuyer:title')}
}

AvgTenderersByBuyer.endpoint = 'averageNumberOfTenderersPerBuyer';
AvgTenderersByBuyer.LABEL_FIELD = '_id';
AvgTenderersByBuyer.VALUE_FIELD = 'numberOfTenderers';

export default AvgTenderersByBuyer;
