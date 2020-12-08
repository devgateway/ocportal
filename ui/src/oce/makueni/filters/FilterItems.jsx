import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";

const FilterItems = ({ep, ...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/items' {...otherProps} />
}

export default FilterItems;
