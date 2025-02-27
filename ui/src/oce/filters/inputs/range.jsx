import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import 'rc-slider/assets/index.css';
import {
  Col, Form, Row,
} from 'react-bootstrap';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
import Slider from 'rc-slider';
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
  titleKey, min, max, minValue, maxValue, onChange,
}) => {
  const { t } = useTranslation();

  if (!min && !max) {
    return null;
  }

  const actualMinValue = minValue || min;
  const actualMaxValue = maxValue || max;

  const handleOnChange = ({ minValue, maxValue }) => onChange({
    minValue: minValue === min ? undefined : minValue,
    maxValue: maxValue === max ? undefined : maxValue,
  });

  return (
    <>
      <Form.Group>
        <Form.Label>{t(titleKey)}</Form.Label>
        <Slider
          range
          allowCross={false}
          min={min}
          max={max}
          defaultValue={[min, max]}
          value={[actualMinValue, actualMaxValue]}
          onChange={([minValue, maxValue]) => handleOnChange({ minValue, maxValue })}
        />
      </Form.Group>

      <Row className="range-inputs">
        <Col md={4}>
          <Form.Group>
            <Form.Label>{t('general:range:min')}</Form.Label>
            <Form.Control
              as={FormattedNumberInput}
              bsSize="sm"
              value={actualMinValue}
              onChange={(value) => handleOnChange({ minValue: value, maxValue })}
            />
          </Form.Group>
        </Col>

        <Col md={4}>
          <Form.Group>
            <Form.Label>{t('general:range:max')}</Form.Label>
            <Form.Control
              as={FormattedNumberInput}
              bsSize="sm"
              value={actualMaxValue}
              onChange={(value) => handleOnChange({ minValue, maxValue: value })}
            />
          </Form.Group>
        </Col>
      </Row>
    </>
  );
};

Range.propTypes = {
  titleKey: PropTypes.string.isRequired,
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
      .then((data) => {
        if (Array.isArray(data) && data.length > 0) {
          const rangeData = data[0];
          if (rangeData[minProperty] !== undefined && rangeData[maxProperty] !== undefined) {
            updateState(() => ({
              min: Math.floor(rangeData[minProperty]),
              max: Math.ceil(rangeData[maxProperty]),
            }));
          }
        }
      })
      .catch((err) => console.error('Failed to fetch range data:', err));
  }, [minProperty, maxProperty, ep]);

  if (state.min !== undefined && state.max !== undefined) {
    return <Range min={state.min} max={state.max} {...otherProps} />;
  }
  return null;
};

RemoteRange.propTypes = {
  ep: PropTypes.string.isRequired,
  titleKey: PropTypes.string.isRequired,
};
