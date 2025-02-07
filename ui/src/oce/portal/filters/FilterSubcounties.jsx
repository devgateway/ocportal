import React from 'react';
import PropTypes from 'prop-types';
import FilterItemTypeAhead from './FilterItemTypeAhead';

const FilterSubcounties = ({ ...otherProps }) => (
  <FilterItemTypeAhead
    ep="/makueni/filters/subcounties"
    {...otherProps}
    mapper={(obj) => ({ _id: obj.id, label: obj.label })}
    multiple
  />
);

FilterSubcounties.propTypes = {
  onChange: PropTypes.func.isRequired,
};

export default FilterSubcounties;
