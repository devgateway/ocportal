import FilterItemSingleSelect from './FilterItemSingleSelect';

class FilterItemFY extends FilterItemSingleSelect {
  
  constructor(props) {
    super(props);
    
  }
}

FilterItemFY.getName = () => 'Fiscal Year';
FilterItemFY.getProperty = () => 'fiscalYear';
FilterItemFY.getEP = () => '/makueni/filters/fiscalYears';

export default FilterItemFY;
