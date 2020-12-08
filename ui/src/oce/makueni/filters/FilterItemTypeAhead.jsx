import {Typeahead} from 'react-bootstrap-typeahead';
import {useEffect, useState} from "react";
import PropTypes from "prop-types";
import {fetch} from "../../api/Api";

const FilterItemTypeAhead = props => {

  const [options, setOptions] = useState([]);
  const [selected, setSelected] = useState([]);

  useEffect(() => {
    fetch(props.ep).then(data => setOptions(data));
  }, [props.ep]);

  useEffect(() => {
    if (props.value) {
      setSelected(options.filter(o => o._id === props.value))
    } else {
      setSelected([]);
    }
  }, [props.value, props.ep]);

  const idProp = (obj) => props.idFunc ? props.idFunc(obj) : obj._id;

  // componentWillReceiveProps(nextProps)
  // {
  //   // filter wards based on selected sub-counties
  //   if (this.constructor.getProperty() === 'ward') {
  //     const newSubcounties = nextProps.localFilters.get('subcounty');
  //     const oldSubcounties = this.props.localFilters.get('subcounty');
  //
  //     if (JSON.stringify(newSubcounties) !== JSON.stringify(oldSubcounties)) {
  //       if (this.state.initialData === undefined) {
  //         this.setState({initialData: this.state.data});
  //       }
  //
  //       if (newSubcounties !== undefined) {
  //         const newData = this.state.initialData !== undefined
  //           ? this.state.initialData.filter(item => newSubcounties.includes(item.subcountyId))
  //           : this.state.data.filter(item => newSubcounties.includes(item.subcountyId));
  //         this.setState({ data: newData });
  //       } else {
  //         this.setState({ data: this.state.initialData });
  //       }
  //
  //       this.setState({ selected: [] });
  //     }
  //   }
  // }
  //
  const handleChange = filterVal => {
    {
      const onChange = props.onChange;
      if (props.multiple) {
        const ids = filterVal.map(item => idProp(item));
        onChange(ids);  //TODO: implement this
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
                 selected={selected}
                 multiple={props.multiple}
      />
  );
}

FilterItemTypeAhead.propTypes = {
  translations: PropTypes.object.isRequired,
  property: PropTypes.string.isRequired,
  ep: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  idFunc: PropTypes.func,
  value: PropTypes.oneOfType([
    PropTypes.array,
    PropTypes.number
  ])
};

export default FilterItemTypeAhead;
