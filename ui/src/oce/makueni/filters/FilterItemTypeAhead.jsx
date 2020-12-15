import {Typeahead} from 'react-bootstrap-typeahead';
import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {fetch} from "../../api/Api";
import {ControlLabel, FormGroup} from "react-bootstrap";
import {identity} from "../../tools";
import {tCreator} from "../../translatable";

const FilterItemTypeAhead = props => {
  const [options, setOptions] = useState([]);

  const mapper = props.mapper || identity;

  useEffect(() => {
    fetch(props.ep, props.epParams).then(data => setOptions(data.map(mapper)));
  }, [props.ep, props.epParams, mapper]);

  const handleChange = filterVal => {
    const onChange = props.onChange;
    if (props.multiple) {
      const ids = filterVal.map(item => item._id);
      onChange(ids);
    } else {
      const id = filterVal.length === 0 ? undefined : filterVal[0]._id;
      onChange(id);
    }
  }

  const t = tCreator(props.translations);

  return (
    <FormGroup>
      <ControlLabel>{props.labelKey && t(props.labelKey)}</ControlLabel>
      <Typeahead id={'filter-' + props.property}
                 onChange={handleChange}
                 options={options === undefined ? [] : options}
                 clearButton={true}
                 placeholder={'Make a selection'}
                 positionFixed={true}
                 selected={props.value ? (options.filter(o => Array.isArray(props.value) ?
                     props.value.includes(o._id) : props.value === o._id)) : []}
                 multiple={props.multiple}
      />
    </FormGroup>
  );
}

FilterItemTypeAhead.propTypes = {
  translations: PropTypes.object.isRequired,
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  multiple: PropTypes.bool,
  idFunc: PropTypes.func,
  mapper: PropTypes.func,
  labelKey: PropTypes.string,
  epParams: PropTypes.object,
  value: PropTypes.oneOfType([
    PropTypes.array,
    PropTypes.number
  ])
};

export default FilterItemTypeAhead;
