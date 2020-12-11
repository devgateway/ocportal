import FilterItemTypeAhead from './FilterItemTypeAhead';
import React from "react";
import PropTypes from "prop-types";


const FilterWards = ({subcounty, ...otherProps}) => {

  return <FilterItemTypeAhead ep='/makueni/filters/wards'
                              epParams={subcounty ? {subcountyIds: subcounty} : {}} {...otherProps}
                              idFunc={(obj) => obj.id} multiple={true}/>
}

FilterWards.propTypes = {
  subcounty: PropTypes.array
};

export default FilterWards;
