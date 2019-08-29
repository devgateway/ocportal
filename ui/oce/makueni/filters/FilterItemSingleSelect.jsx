import translatable from '../../translatable';
import { ppState } from '../procurementPlan/state';
import { API_ROOT } from '../../state/oce-state';
import { ControlLabel, FormControl, FormGroup } from 'react-bootstrap';

class FilterItemSingleSelect extends translatable(React.Component) {
  constructor(props) {
    super(props);
    
    this.state = {
      selected: 'all'
    };
    
    this.filterEP = ppState.input({
      name: 'filter' + this.constructor.getName() + 'EP',
      initial: `${API_ROOT}` + this.constructor.getEP(),
    });
    
    this.filterRemote = ppState.remote({
      name: 'filter' + this.constructor.getName() + 'Remote',
      url: this.filterEP,
    });
    
    this.filterData = ppState.mapping({
      name: 'filter' + this.constructor.getName() + 'Data',
      deps: [this.filterRemote],
      mapper: data => this.setState({ data: data })
    });
    
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
    if (this.state.data === undefined) {
      this.filterData.getState(this.constructor.getName());
    }
    
    this.props.filters.addListener(this.constructor.getName(), () => this.updateBindings());
  }
  
  componentWillUnmount() {
    this.props.filters.removeListener(this.constructor.getName());
  }
  
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
  
  handleChange(e) {
    const { filters, onUpdate } = this.props;
    const filterVal = e.target.value;
    
    filters.getState()
    .then(value => {
      if (filterVal === 'all') {
        if (onUpdate === undefined) {
          filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), undefined));
        } else {
          onUpdate(this.constructor.getProperty(), undefined);
        }
      } else {
        if (onUpdate === undefined) {
          filters.assign(this.constructor.getName(), value.set(this.constructor.getProperty(), filterVal));
        } else {
          onUpdate(this.constructor.getProperty(), filterVal);
        }
      }
    });
    
    this.setState({ selected: filterVal });
  }
  
  render() {
    const { data, selected } = this.state;
    
    return (
      <FormGroup>
        <ControlLabel>Select a Value</ControlLabel>
        <FormControl componentClass="select" placeholder="select" onChange={this.handleChange}
                     value={selected}>
          <option value="all">All</option>
          {
            data !== undefined
              ? data.map(item => <option key={item.label} value={item._id}>{item.label}</option>)
              : null
          }
        </FormControl>
      </FormGroup>
    );
  }
}

export default FilterItemSingleSelect;
