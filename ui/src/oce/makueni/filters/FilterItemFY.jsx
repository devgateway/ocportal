import React from "react";
import FilterItemSingleSelect from './FilterItemSingleSelect';

const FilterItemFY = ({ep, ...otherProps}) => {
  return <FilterItemSingleSelect ep='/makueni/filters/fiscalYears' {...otherProps} />
}

export default FilterItemFY;
