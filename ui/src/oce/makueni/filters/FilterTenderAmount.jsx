import React from 'react';
import PropTypes from 'prop-types';
import { Range } from '../../filters/inputs/range';

const FilterTenderAmount = ({ ...otherProps }) => <Range {...otherProps} titleKey="filters:tenderPrice:title" min={0} max={100000000} />;

FilterTenderAmount.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
};

export default FilterTenderAmount;
