import React from "react";
import {FormControl, FormGroup} from 'react-bootstrap';
import PropTypes from "prop-types";

const FilterInput = props => {
    const value = props.value || '';
    const handleChange = e => props.onChange(e.target.value);

    return (<FormGroup>
        <FormControl
            type="text"
            value={value}
            placeholder="Enter search term"
            onChange={handleChange}
        />
    </FormGroup>);
}

FilterInput.propTypes = {
    translations: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default FilterInput;
