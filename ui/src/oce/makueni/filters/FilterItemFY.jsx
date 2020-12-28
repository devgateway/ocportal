import React from 'react';
import PropTypes from 'prop-types';
import FilterItemSingleSelect from './FilterItemSingleSelect';

const FilterItemFY = ({ ...otherProps }) => <FilterItemSingleSelect ep="/makueni/filters/fiscalYears" {...otherProps} />;

FilterItemFY.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
};

export default FilterItemFY;
