import FilterItemTypeAhead from './FilterItemTypeAhead';

class FilterSubcounties extends FilterItemTypeAhead {
  
  constructor(props) {
    super(props);
  
    this.state.multiple = true;
  }
}

FilterSubcounties.getName = () => 'Sub-Counties';
FilterSubcounties.getProperty = () => 'subcounty';
FilterSubcounties.getEP = () => '/makueni/filters/subcounties';

export default FilterSubcounties;
