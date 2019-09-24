import Tab from './index';
import BuyerSelect from '../buyer-select.jsx';
import ProcuringEntitySelect from '../procuring-entity-select';
import SupplierSelect from '../supplier-select';

class Organizations extends Tab {
  render() {
    return <div>
      {this.constructor.FILTERS.map(([name, component]) => this.renderChild(component, name))}
    </div>;
  }
}

Organizations.getName = t => t('filters:tabs:organizations:title');

Organizations.FILTERS = [
  ['buyerId', BuyerSelect],
  ['procuringEntityId', ProcuringEntitySelect],
  ['supplierId', SupplierSelect]
];

export default Organizations;
