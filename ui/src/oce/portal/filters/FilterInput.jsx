import React, { useEffect, useState } from 'react';
import { Form } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';

const MIN_LENGTH = 3;
const MAX_LENGTH = 255;
const EMPTY_STRING = '';

const FilterInput = (props) => {
  const { value } = props;
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
  const { t } = useTranslation();

  return (
    <Form.Group>
      <Form.Control
        type="text"
        value={internalValue || ''}
        maxLength={MAX_LENGTH}
        placeholder="Enter search term"
        onChange={handleChange}
      />
      {internalValue?.length < MIN_LENGTH && (
        <Form.Text className="text-danger" aria-live="polite">
          {t('filters:text:minLength', { minLength: MIN_LENGTH })}
        </Form.Text>
      )}
    </Form.Group>
  );
};

FilterInput.propTypes = {
  onChange: PropTypes.func.isRequired,
  value: PropTypes.string,
};

FilterInput.defaultProps = {
  value: '',
};

export default FilterInput;
