import React from 'react';
import PropTypes from 'prop-types';
import FilterItemSingleSelect from './FilterItemSingleSelect';

const FilterItemDep = ({ ...otherProps }) => <FilterItemSingleSelect ep="/makueni/filters/departments" {...otherProps} />;

FilterItemDep.propTypes = {
  onChange: PropTypes.func.isRequired,
};

export default FilterItemDep;
