import translatable from '../../translatable';
import { FormControl, FormGroup } from 'react-bootstrap';
import { delayUserInput } from '../tenders/state';

class FilterInput extends translatable(React.Component) {
  constructor(props) {
    super(props);
    
    this.state = {
      value: ''
    };
    
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
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
      if (item.get('text') === undefined) {
        this.setState({ value: '' });
      }
    });
  }
  
  handleChange(e) {
    const { filters } = this.props;
    const inputValue = e.target.value;
    
    const self = this;
    delayUserInput('amount', function () {
      filters.getState()
      .then(value => {
        filters.assign(self.constructor.getName(), value.set('text', inputValue));
      });
    }, 2000);
    
    this.setState({ value: inputValue });
  }
  
  render() {
    const { value } = this.state;
    
    return (<FormGroup>
      <FormControl
        type="text"
        value={value}
        placeholder="Enter search term"
        onChange={this.handleChange}
      />
    </FormGroup>);
  }
}

export default FilterInput;
