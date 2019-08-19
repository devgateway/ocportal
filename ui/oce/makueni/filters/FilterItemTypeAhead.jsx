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
  
  handleChange(filterVal) {
    const { filters } = this.props;
  
    filters.getState()
    .then(value => {
      if (filterVal.length === 0) {
        filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), undefined));
        this.setState({ selected: [] });
      } else {
        // do we have a multi select option?
        if (this.state.multiple !== undefined && this.state.multiple === true) {
          const ids = filterVal
          .map(item => item._id !== undefined ? item._id : item.id);
  
          filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), ids));
          this.setState({ selected: filterVal });
        } else {
          const id = filterVal[0]._id !== undefined ? filterVal[0]._id : filterVal[0].id;
          
          filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), id));
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
