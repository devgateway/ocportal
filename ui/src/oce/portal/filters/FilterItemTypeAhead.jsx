import { Typeahead } from 'react-bootstrap-typeahead';
import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { fetch } from '../../api/Api';
import { identity } from '../../tools';

const FilterItemTypeAhead = (props) => {
  const [options, setOptions] = useState([]);

  const mapper = props.mapper || identity;

  useEffect(() => {
    fetch(props.ep, props.epParams)
      .then((data) => setOptions(data.map(mapper)))
      .catch((err) => {
        console.error('Failed to fetch options:', err);
        setOptions([]);
      });
  }, [props.ep, props.epParams, mapper]);

  const handleChange = (filterVal) => {
    const { onChange } = props;
    if (props.multiple) {
      const ids = filterVal.map((item) => item._id);
      onChange(ids);
    } else {
      const id = filterVal.length === 0 ? undefined : filterVal[0]._id;
      onChange(id);
    }
  };

  const { t } = useTranslation();

  return (
    <Form.Group>
      <Form.Label>{props.labelKey && t(props.labelKey)}</Form.Label>
      <Typeahead
        id={`filter-${props.property}`}
        onChange={handleChange}
        options={options === undefined ? [] : options}
        clearButton
        placeholder="Make a selection"
        positionFixed
        selected={props.value ? (options.filter((o) => (Array.isArray(props.value)
          ? props.value.includes(o._id) : props.value === o._id))) : []}
        multiple={props.multiple}
      />
    </Form.Group>
  );
};

FilterItemTypeAhead.propTypes = {
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  multiple: PropTypes.bool,
  mapper: PropTypes.func,
  labelKey: PropTypes.string,
  epParams: PropTypes.object,
  value: PropTypes.oneOfType([
    PropTypes.array,
    PropTypes.number,
  ]),
};

export default FilterItemTypeAhead;
