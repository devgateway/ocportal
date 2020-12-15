import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";
import PropTypes from "prop-types";

const FilterSubcounties = ({...otherProps}) => {
  return <FilterItemTypeAhead ep='/makueni/filters/subcounties' {...otherProps}
                              mapper={obj => ({_id: obj.id, label: obj.label})} multiple={true}/>
}

FilterSubcounties.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};


export default FilterSubcounties;
