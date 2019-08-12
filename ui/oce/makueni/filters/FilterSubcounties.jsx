import FilterItemTypeAhead from './FilterItemTypeAhead';

class FilterSubcounties extends FilterItemTypeAhead {
  
  constructor(props) {
    super(props);
    
  }
}

FilterSubcounties.getName = () => 'Sub-Counties';
FilterSubcounties.getProperty = () => 'subcounty';
FilterSubcounties.getEP = () => '/makueni/filters/subcounties';

export default FilterSubcounties;
