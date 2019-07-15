import { API_ROOT } from '../../state/oce-state';
import { ppState } from '../procurementPlan/state';
import FilterItem from './FilterItem';
import { ControlLabel, FormControl, FormGroup } from 'react-bootstrap';

class FilterItemDep extends FilterItem {
  
  constructor(props) {
    super(props);
    
    this.state = {
      selected: 'all'
    };
    
    this.filterEP = ppState.input({
      name: 'filterDepEP',
      initial: `${API_ROOT}/makueni/filters/departments`,
    });
    
    this.filterRemote = ppState.remote({
      name: 'filterDepRemote',
      url: this.filterEP,
    });
    
    this.filterData = ppState.mapping({
      name: 'filterDepData',
      deps: [this.filterRemote],
      mapper: data => this.setState({ data: data })
    });
  
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
    if (this.state.data === undefined) {
      this.filterData.getState(this.constructor.getName());
    }
  
    this.props.filters.addListener('[[FilterItemDep]]', () => this.updateBindings());
  }
  
  componentWillUnmount() {
    this.props.filters.removeListener('[[FilterItemDep]]');
  }
  
  updateBindings() {
    Promise.all([
      this.props.filters.getState('[[FilterItemDep]]'),
    ])
    .then(([item]) => {
      // update internal state on reset
      if (item.get('department') === undefined) {
        this.setState({ selected: 'all' })
      }
    });
  }
  
  handleChange(e) {
    const { filters } = this.props;
    const filterVal = e.target.value;
    
    filters.getState()
    .then(value => {
      if (filterVal === 'all') {
        filters.assign('[[FilterItemDep]]', value.set('department', undefined));
      } else {
        filters.assign('[[FilterItemDep]]', value.set('department', filterVal));
      }
    });
    
    this.setState({ selected: filterVal })
  }
  
  render() {
    const { data, selected } = this.state;
    
    return (
      <FormGroup>
        <ControlLabel>Select a Department</ControlLabel>
        <FormControl componentClass="select" placeholder="select" onChange={this.handleChange} value={selected}>
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

FilterItemDep.getName = () => 'Departments';

export default FilterItemDep;
