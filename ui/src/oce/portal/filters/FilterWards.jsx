import React from 'react';
import PropTypes from 'prop-types';
import FilterItemTypeAhead from './FilterItemTypeAhead';

const FilterWards = ({ subcounty, ...otherProps }) => (
  <FilterItemTypeAhead
    ep="/makueni/filters/wards"
    epParams={subcounty ? { subcountyIds: subcounty } : {}}
    {...otherProps}
    mapper={(obj) => ({ _id: obj.id, label: obj.label })}
    multiple
  />
);

FilterWards.propTypes = {
  subcounty: PropTypes.array,
  onChange: PropTypes.func.isRequired,
};

export default FilterWards;
