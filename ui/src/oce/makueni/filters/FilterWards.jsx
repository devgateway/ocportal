import FilterItemTypeAhead from './FilterItemTypeAhead';

class FilterWards extends FilterItemTypeAhead {
  
  constructor(props) {
    super(props);
    
    this.state.multiple = true;
  }
}

FilterWards.getName = () => 'Wards';
FilterWards.getProperty = () => 'ward';
FilterWards.getEP = () => '/makueni/filters/wards';

export default FilterWards;
