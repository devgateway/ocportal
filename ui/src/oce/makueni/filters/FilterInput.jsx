import React, { useEffect, useState } from 'react';
import { FormControl, FormGroup, HelpBlock } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { tCreator } from '../../translatable';

const MIN_LENGTH = 3;
const MAX_LENGTH = 255;
const EMPTY_STRING = '';

const FilterInput = (props) => {
  const { value, translations } = props;
  const [internalValue, setInternalValue] = useState(value);

  useEffect(() => {
    setInternalValue(value);
  }, [value]);

  const handleChange = (e) => {
    const { value: newValue } = e.target;
    setInternalValue(newValue);
    const externalValue = (!newValue || newValue.length < MIN_LENGTH) ? EMPTY_STRING : newValue;
    props.onChange(externalValue);
  };
  const t = tCreator(translations);

  return (
    <FormGroup>
      <FormControl
        type="text"
        value={internalValue}
        maxLength={MAX_LENGTH}
        placeholder="Enter search term"
        onChange={handleChange}
      />
      {internalValue && internalValue.length < MIN_LENGTH
      && <HelpBlock>{t('filters:text:minLength').replace('$#$', MIN_LENGTH)}</HelpBlock>}
    </FormGroup>
  );
};

FilterInput.propTypes = {
  onChange: PropTypes.func.isRequired,
  value: PropTypes.string,
  translations: PropTypes.object.isRequired,
};

FilterInput.defaultProps = {
  value: '',
};

export default FilterInput;
