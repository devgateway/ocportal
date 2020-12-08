import React, {useEffect, useState} from "react";
import {FormControl, FormGroup} from 'react-bootstrap';
import PropTypes from "prop-types";

const FilterInput = props => {
    const [data, setData] = useState(props.value);

    useEffect(() => {
        setData(props.value === undefined ? '' : props.value);
    }, [props.value]);

    const handleChange = e => {
        const {onChange} = props;
        const inputValue = e.target.value;
        onChange(inputValue);
        setData(inputValue);
    }

    return (<FormGroup>
        <FormControl
            type="text"
            value={data}
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
