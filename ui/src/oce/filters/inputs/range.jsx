import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { tCreator } from '../../translatable';
import RCRange from 'rc-slider/lib/Range';
import 'rc-slider/assets/index.css';
import {
  Col, ControlLabel, FormControl, FormGroup, Row,
} from 'react-bootstrap';
import { useImmer } from 'use-immer';
import { fetch } from '../../api/Api';

const FormattedNumberInput = ({ value, onChange, ...otherProps }) => {
  const formattedValue = value && value.toLocaleString();

  const handleChange = (e) => {
    const value = parseFloat(e.target.value.replace(/,/g, ''));
    onChange(value);
  };

  return (
    <input
      {...otherProps}
      value={formattedValue}
      onChange={handleChange}
    />
  );
};

export const Range = ({
  titleKey, min, max, minValue, maxValue, translations, onChange,
}) => {
  if (!min && !max) {
    return null;
  }

  const actualMinValue = minValue || min;
  const actualMaxValue = maxValue || max;
  const t = tCreator(translations);

  const handleOnChange = ({ minValue, maxValue }) => onChange({
    minValue: minValue === min ? undefined : minValue,
    maxValue: maxValue === max ? undefined : maxValue,
  });

  return (
    <>
      <FormGroup>
        <ControlLabel>{t(titleKey)}</ControlLabel>
        <RCRange
          allowCross={false}
          min={min}
          max={max}
          defaultValue={[min, max]}
          value={[actualMinValue, actualMaxValue]}
          onChange={([minValue, maxValue]) => handleOnChange({ minValue, maxValue })}
        />
      </FormGroup>

      <Row className="range-inputs">
        <Col md={4}>
          <FormGroup>
            <ControlLabel>{t('general:range:min')}</ControlLabel>
            <FormControl
              componentClass={FormattedNumberInput}
              bsSize="sm"
              value={actualMinValue}
              onChange={(value) => handleOnChange({ minValue: value, maxValue })}
            />
          </FormGroup>
        </Col>

        <Col md={4}>
          <FormGroup>
            <ControlLabel>{t('general:range:max')}</ControlLabel>
            <FormControl
              componentClass={FormattedNumberInput}
              bsSize="sm"
              value={actualMaxValue}
              onChange={(value) => handleOnChange({ minValue, maxValue: value })}
            />
          </FormGroup>
        </Col>
      </Row>
    </>
  );
};

Range.propTypes = {
  titleKey: PropTypes.string.isRequired,
  translations: PropTypes.object.isRequired,
  min: PropTypes.number.isRequired,
  max: PropTypes.number.isRequired,
  minValue: PropTypes.number,
  maxValue: PropTypes.number,
};

export const RemoteRange = ({
  ep, minProperty, maxProperty, ...otherProps
}) => {
  const [state, updateState] = useImmer({});

  useEffect(() => {
    fetch(ep)
      .then(([{ [minProperty]: min, [maxProperty]: max }]) => updateState(() => ({
        min: Math.floor(min),
        max: Math.ceil(max),
      })));
  }, [minProperty, maxProperty, ep]);

  if (state.min !== undefined && state.max !== undefined) {
    return <Range min={state.min} max={state.max} {...otherProps} />;
  }
  return null;
};

RemoteRange.propTypes = {
  ep: PropTypes.string.isRequired,
  titleKey: PropTypes.string.isRequired,
  translations: PropTypes.object.isRequired,
};

export default RemoteRange;
