import FilterItemSingleSelect from './FilterItemSingleSelect';

class FilterItemDep extends FilterItemSingleSelect {
  
  constructor(props) {
    super(props);
    
  }
}

FilterItemDep.getName = () => 'Departments';
FilterItemDep.getProperty = () => 'department';
FilterItemDep.getEP = () => '/makueni/filters/departments';

export default FilterItemDep;
