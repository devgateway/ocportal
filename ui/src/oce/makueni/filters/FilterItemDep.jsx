import React from "react";
import FilterItemSingleSelect from './FilterItemSingleSelect';

const FilterItemDep = ({ep, ...otherProps}) => {
  return <FilterItemSingleSelect ep='/makueni/filters/departments' {...otherProps} />
}

export default FilterItemDep;
