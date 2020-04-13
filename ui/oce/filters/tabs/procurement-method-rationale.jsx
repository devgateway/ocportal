import Tab from './index';
import ProcurementMethod from '../procurement-method';
import ProcurementMethodRationale from '../procurement-method-rationale';

class ProcurementMethodRationaleRules extends Tab {
  render() {
    return (
      <div>
        {this.renderChild(ProcurementMethodRationale, 'procurementMethodRationale')}
      </div>
    );
  }
}

ProcurementMethodRationaleRules.getName = t => t('filters:tabs:procurementMethodRationale:title');

export default ProcurementMethodRationaleRules;
