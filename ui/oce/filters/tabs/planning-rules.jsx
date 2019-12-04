import Tab from './index';
import FiscalYear from '../fiscal-year';

class PlanningRules extends Tab {
  render() {
    return (
      <div>
        {this.renderChild(FiscalYear, 'fiscalYear')}
      </div>
    );
  }
}

PlanningRules.getName = t => t('filters:tabs:fiscalYear:title');

export default PlanningRules;
