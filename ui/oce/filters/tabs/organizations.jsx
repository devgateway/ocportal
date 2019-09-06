import Tab from './index';
import ProcuringEntity from '../procuring-entity';
import Supplier from '../supplier.jsx';
import Buyer from '../buyer';

class Organizations extends Tab {
  render() {
    return <div>
      {this.constructor.FILTERS.map(([name, component]) => this.renderChild(component, name))}
    </div>;
  }
}

Organizations.getName = t => t('filters:tabs:organizations:title');

Organizations.FILTERS = [
  ['buyerId', Buyer],
  ['procuringEntityId', ProcuringEntity],
  ['supplierId', Supplier]
];

export default Organizations;
