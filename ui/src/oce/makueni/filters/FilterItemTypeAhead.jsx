import {Typeahead} from 'react-bootstrap-typeahead';
import {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {fetch} from "../../api/Api";

const FilterItemTypeAhead = props => {
  const [options, setOptions] = useState([]);
  const idProp = (obj) => props.idFunc ? props.idFunc(obj) : obj._id;

  useEffect(() => {
    fetch(props.ep, props.epParams).then(data => setOptions(data));
  }, [props.ep, props.epParams]);

  const handleChange = filterVal => {
    {
      const onChange = props.onChange;
      if (props.multiple) {
        const ids = filterVal.map(item => idProp(item));
        onChange(ids);
      } else {
        const id = filterVal.length === 0 ? undefined : idProp(filterVal[0]);
        onChange(id);
      }
    }
  }

  return (
      <Typeahead id={'filter-' + props.property}
                 onChange={handleChange}
                 options={options === undefined ? [] : options}
                 clearButton={true}
                 placeholder={'Make a selection'}
                 selected={props.value ? (options.filter(o => Array.isArray(props.value) ?
                     props.value.includes(idProp(o)) : props.value === idProp(o))) : []}
                 multiple={props.multiple}
      />
  );
}

FilterItemTypeAhead.propTypes = {
  translations: PropTypes.object.isRequired,
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  multiple: PropTypes.bool,
  idFunc: PropTypes.func,
  epParams: PropTypes.object,
  value: PropTypes.oneOfType([
    PropTypes.array,
    PropTypes.number
  ])
};

export default FilterItemTypeAhead;
