import translatable from '../../translatable';
import { FormControl, FormGroup, HelpBlock } from 'react-bootstrap';
import { delayUserInput } from '../tenders/state';

const MIN_LENGTH = 3;
const MAX_LENGTH = 255;

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
    const { filters, onUpdate } = this.props;
    const inputValue = e.target.value;
    const externalValue = (!inputValue || inputValue.length < MIN_LENGTH) ? null : inputValue;
    
    const self = this;
    delayUserInput('amount', function () {
      filters.getState()
      .then(value => {
        if (onUpdate === undefined) {
          filters.assign(self.constructor.getName(), value.set('text', externalValue));
        } else {
          onUpdate('text', externalValue);
        }
      });
    }, 100);
    
    this.setState({ value: inputValue });
  }
  
  render() {
    const { value } = this.state;

    return (<FormGroup>
      <FormControl
        type="text"
        value={value}
        maxLength={MAX_LENGTH}
        placeholder="Enter search term"
        onChange={this.handleChange}
      />
      {value && value.length < MIN_LENGTH &&
      <HelpBlock>{this.t('filters:text:minLength').replace('$#$', MIN_LENGTH)}</HelpBlock>}
    </FormGroup>);
  }
}

export default FilterInput;
