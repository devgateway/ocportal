import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";
import PropTypes from "prop-types";

const FilterWards = ({subcounty, ...otherProps}) => {

  return <FilterItemTypeAhead ep='/makueni/filters/wards'
                              epParams={subcounty ? {subcountyIds: subcounty} : {}} {...otherProps}
                              mapper={obj => ({_id: obj.id, label: obj.label})} multiple={true}/>
}

FilterWards.propTypes = {
  subcounty: PropTypes.array,
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};

export default FilterWards;
