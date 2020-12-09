import {FormControl, FormGroup, ControlLabel} from 'react-bootstrap';
import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {fetch} from "../../api/Api";

const FilterItemSingleSelect = props => {

  const [data, setData] = useState([]);
  const value = props.value || 'all';

  const handleChange = e => {
    const newValue = e.target.value;
    props.onChange(newValue === 'all' ? undefined : newValue);
  }

  useEffect(() => {
    fetch(props.ep).then(data => setData(data));
  }, [props.ep]);

  return (
    <FormGroup>
      <ControlLabel>Select a Value</ControlLabel>
      <FormControl componentClass="select" placeholder="select" onChange={handleChange} value={value}>
        <option value="all">All</option>
        {
          data.map(item => <option key={item.label} value={item._id}>{item.label}</option>)
        }
      </FormControl>
    </FormGroup>
  );
}

FilterItemSingleSelect.propTypes = {
  translations: PropTypes.object.isRequired,
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired
};

export default FilterItemSingleSelect;
