import { Typeahead } from 'react-bootstrap-typeahead';
import FilterItemSingleSelect from './FilterItemSingleSelect';

class FilterItemTypeAhead extends FilterItemSingleSelect {
  constructor(props) {
    super(props);
    
    this.state = {
      selected: []
    };
  }
  
  updateBindings() {
    Promise.all([
      this.props.filters.getState(this.constructor.getName()),
    ])
    .then(([item]) => {
      // update internal state on reset
      if (item.get(this.constructor.getProperty()) === undefined) {
        this.setState({ selected: [] });
      }
    });
  }
  
  componentWillReceiveProps(nextProps) {
    // filter wards based on selected sub-counties
    if (this.constructor.getProperty() === 'ward') {
      const newSubcounties = nextProps.localFilters.get('subcounty');
      const oldSubcounties = this.props.localFilters.get('subcounty');
      
      if (JSON.stringify(newSubcounties) !== JSON.stringify(oldSubcounties)) {
        if (this.state.initialData === undefined) {
          this.setState({ initialData: this.state.data });
        }
        
        if (newSubcounties !== undefined) {
          const newData = this.state.initialData !== undefined
            ? this.state.initialData.filter(item => newSubcounties.includes(item.subcountyId))
            : this.state.data.filter(item => newSubcounties.includes(item.subcountyId));
          this.setState({ data: newData });
        } else {
          this.setState({ data: this.state.initialData });
        }
        
        this.setState({ selected: [] });
      }
    }
  }
  
  handleChange(filterVal) {
    const { filters, onUpdate } = this.props;
    
    filters.getState()
    .then(value => {
      if (filterVal.length === 0) {
        if (onUpdate === undefined) {
          filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), undefined));
        } else {
          onUpdate(this.constructor.getProperty(), undefined);
        }
        this.setState({ selected: [] });
      } else {
        // do we have a multi select option?
        if (this.state.multiple !== undefined && this.state.multiple === true) {
          const ids = filterVal
          .map(item => item._id !== undefined ? item._id : item.id);
          
          if (onUpdate === undefined) {
            filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), ids));
          } else {
            onUpdate(this.constructor.getProperty(), ids);
          }
          this.setState({ selected: filterVal });
        } else {
          const id = filterVal[0]._id !== undefined ? filterVal[0]._id : filterVal[0].id;
          
          if (onUpdate === undefined) {
            filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), id));
          } else {
            onUpdate(this.constructor.getProperty(), id);
          }
          
          this.setState({ selected: [filterVal[0]] });
        }
      }
    });
  }
  
  render() {
    const { data, selected } = this.state;
    let multiple = false;
    if (this.state.multiple !== undefined) {
      multiple = this.state.multiple;
    }
    
    return (
      <Typeahead id={'filter-' + this.constructor.getProperty()}
                 onChange={this.handleChange}
                 options={data === undefined ? [] : data}
                 clearButton={true}
                 placeholder={'Make a selection'}
                 selected={selected}
                 multiple={multiple}
      />
    );
  }
}

export default FilterItemTypeAhead;
