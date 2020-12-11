import React from "react";
import FilterItemSingleSelect from './FilterItemSingleSelect';
import PropTypes from "prop-types";

const FilterItemFY = ({...otherProps}) => {
  return <FilterItemSingleSelect ep='/makueni/filters/fiscalYears' {...otherProps} />
}

FilterItemFY.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};

export default FilterItemFY;
