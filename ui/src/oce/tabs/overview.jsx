import Tab from './index';
import OverviewChart from '../visualizations/charts/overview';
import TopSuppliersTable from '../visualizations/tables/top-suppliers';
import TendersTable from '../visualizations/tables/tenders';
import AwardsTable from '../visualizations/tables/awards';
import ExpenditureTodateVsBudget from '../visualizations/charts/expenditure-todate-vs-budget';

class Overview extends Tab {
  static getName(t) {
    return t('tabs:overview:title');
  }
}

Overview.visualizations = [OverviewChart, TopSuppliersTable, TendersTable, AwardsTable,
  ExpenditureTodateVsBudget];

Overview.icon = 'overview';

export default Overview;
