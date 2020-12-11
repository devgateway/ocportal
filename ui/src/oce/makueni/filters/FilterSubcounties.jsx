import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";
import PropTypes from "prop-types";
import FilterItems from "./FilterItems";

const FilterSubcounties = ({...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/subcounties' {...otherProps}
                              idFunc={(obj) => obj.id} multiple={true}/>
}

FilterItems.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};


export default FilterSubcounties;
