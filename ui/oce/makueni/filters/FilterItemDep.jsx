import { API_ROOT } from '../../state/oce-state';
import { ppState } from '../procurementPlan/state';
import FilterItem from './FilterItem';
import { ControlLabel, FormControl, FormGroup } from 'react-bootstrap';
import { PEInfo } from '../../corruption-risk/procuring-entities/single/state';

class FilterItemDep extends FilterItem {
  
  constructor(props) {
    super(props);
    
    this.state = {
      loaded: false
    };
    
    this.filterEP = ppState.input({
      name: 'filterEP',
      initial: `${API_ROOT}/makueni/filters/departments`,
    });
    
    this.filterRemote = ppState.remote({
      name: 'filterRemote',
      url: this.filterEP,
    });
    
    this.filterData = ppState.mapping({
      name: 'filterData',
      deps: [this.filterRemote],
      mapper: data => this.setState({ data: data })
    });
    
    console.log('>>>>>>');
    
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
    if (!this.state.loaded) {
      // TODO - change this in upper class
      this.filterData.getState(this.constructor.getName());
      
      this.setState({ loaded: true });
    }
  }
  
  handleChange(e) {
    const { filters } = this.props;
    const filterVal = e.target.value;
  
    filters.getState().then(value => {
      if (filterVal === 'all') {
        filters.assign('Department Filter', value.set("department", undefined));
      } else {
        filters.assign('Department Filter', value.set("department", filterVal));
      }
    });
    
    
  }
  
  render() {
    const { data } = this.state;
    
    console.log(this.state);
    
    return (
      <FormGroup controlId="formControlsSelect">
        <ControlLabel>Select a Department</ControlLabel>
        <FormControl componentClass="select" placeholder="select" onChange={this.handleChange}>
          <option value="all">All</option>
          {
            data !== undefined
              ? data.map(item => <option key={item.code} value={item._id}>{item.label}</option>)
              : null
          }
          {/*<option value="select">select</option>*/}
          {/*<option value="other">...</option>*/}
        </FormControl>
      </FormGroup>
    );
  }
}

FilterItemDep.getName = () => 'Departments';

export default FilterItemDep;
