import { FormControl, FormGroup, ControlLabel } from 'react-bootstrap';
import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { fetch } from '../../api/Api';

const FilterItemSingleSelect = (props) => {
  const [data, setData] = useState([]);
  const value = props.value || 'all';

  const handleChange = (e) => {
    const newValue = e.target.value;
    props.onChange(newValue === 'all' ? undefined : newValue);
  };

  useEffect(() => {
    fetch(props.ep).then((data) => setData(data));
  }, [props.ep]);

  const { t } = useTranslation();

  const label = props.labelKey ? t(props.labelKey) : 'Select a Value';

  const { itemValueKey = '_id', itemLabelKey = 'label' } = props;

  return (
    <FormGroup>
      <ControlLabel>{label}</ControlLabel>
      <FormControl componentClass="select" placeholder="select" onChange={handleChange} value={value}>
        <option value="all">All</option>
        {
          data.map((item) => <option key={item[itemValueKey]} value={item[itemValueKey]}>{item[itemLabelKey]}</option>)
        }
      </FormControl>
    </FormGroup>
  );
};

FilterItemSingleSelect.propTypes = {
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

export default FilterItemSingleSelect;
