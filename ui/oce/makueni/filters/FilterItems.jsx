import FilterItemTypeAhead from './FilterItemTypeAhead';

class FilterItems extends FilterItemTypeAhead {
  
  constructor(props) {
    super(props);
    
  }
}

FilterItems.getName = () => 'Items';
FilterItems.getProperty = () => 'items';
FilterItems.getEP = () => '/makueni/filters/items';

export default FilterItems;
