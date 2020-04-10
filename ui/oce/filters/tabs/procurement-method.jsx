import Tab from './index';
import ProcurementMethod from '../procurement-method';

class ProcurementMethodRules extends Tab {
  render() {
    return (
      <div>
        {this.renderChild(ProcurementMethod, 'procurementMethod')}
      </div>
    );
  }
}

ProcurementMethodRules.getName = t => t('filters:tabs:procurementMethod:title');

export default ProcurementMethodRules;
