import React from 'react';
import PropTypes from 'prop-types';
import FilterItemTypeAhead from './FilterItemTypeAhead';

const FilterItems = ({ ...otherProps }) => <FilterItemTypeAhead ep="/makueni/filters/items" {...otherProps} />;

FilterItems.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
};

export default FilterItems;
