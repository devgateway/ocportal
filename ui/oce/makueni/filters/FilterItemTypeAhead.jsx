import { Typeahead } from 'react-bootstrap-typeahead';
import FilterItemSingleSelect from './FilterItemSingleSelect';

class FilterItemTypeAhead extends FilterItemSingleSelect {
  updateBindings() {
    Promise.all([
      this.props.filters.getState(this.constructor.getName()),
    ])
    .then(([item]) => {
      // update internal state on reset
      if (item.get(this.constructor.getProperty()) === undefined) {
        this.setState({ selected: 'all' });
      }
    });
  }
  
  handleChange(filterVal) {
    console.log(filterVal);
    const { filters } = this.props;
    
    filters.getState()
    .then(value => {
      if (filterVal.length === 0) {
        filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), undefined));
        this.setState({ selected: undefined });
      } else {
        filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), filterVal[0]._id));
        this.setState({ selected: filterVal[0]._id });
      }
    });
  }
  
  render() {
    const { data } = this.state;
    
    return (
      <Typeahead id="filter-items"
                 onChange={this.handleChange}
                 options={data === undefined ? [] : data}
                 clearButton={true}
                 placeholder={"Make a selection"}
                 multiple={false}
      />
    );
  }
}

export default FilterItemTypeAhead;
