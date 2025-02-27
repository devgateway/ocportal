import React from 'react';
import PropTypes from 'prop-types';
import FilterItemSingleSelect from './FilterItemSingleSelect';

const FilterItemDep = ({ ...otherProps }) => <FilterItemSingleSelect ep="/client/filters/departments" {...otherProps} />;

FilterItemDep.propTypes = {
  onChange: PropTypes.func.isRequired,
};

export default FilterItemDep;
