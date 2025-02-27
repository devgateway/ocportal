import React from 'react';
import PropTypes from 'prop-types';
import FilterItemTypeAhead from './FilterItemTypeAhead';

const FilterItems = ({ ...otherProps }) => <FilterItemTypeAhead ep="/client/filters/items" {...otherProps} />;

FilterItems.propTypes = {
  onChange: PropTypes.func.isRequired,
};

export default FilterItems;
