import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";


const FilterWards = ({ep, ...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/wards' {...otherProps}
                              idFunc={(obj) => obj.id} multiple={true}/>
}

export default FilterWards;
