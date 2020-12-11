import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";
import PropTypes from "prop-types";

const FilterItems = ({...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/items' {...otherProps} />
}

FilterItems.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};

export default FilterItems;
