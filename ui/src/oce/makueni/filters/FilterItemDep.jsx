import React from "react";
import FilterItemSingleSelect from './FilterItemSingleSelect';
import PropTypes from "prop-types";

const FilterItemDep = ({...otherProps}) => {
  return <FilterItemSingleSelect ep='/makueni/filters/departments' {...otherProps} />
}

FilterItemDep.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};

export default FilterItemDep;
