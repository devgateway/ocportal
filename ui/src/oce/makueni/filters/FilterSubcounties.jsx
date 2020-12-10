import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";

const FilterSubcounties = ({ep, ...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/subcounties' {...otherProps}
                              idFunc={(obj) => obj.id} multiple={true}/>
}

export default FilterSubcounties;
