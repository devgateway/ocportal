import translatable from '../../translatable';
import RCRange from 'rc-slider/lib/Range';
import { FormattedNumberInput } from '../../filters/inputs/range';
import { max } from '../../tools';
import { delayUserInput } from '../tenders/state';

class FilterRange extends translatable(React.Component) {
  constructor(props) {
    super(props);
    
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
      const { minValue, maxValue } = this.state;
      
      // update internal state on reset
      if (item.get('min') === undefined && item.get('max') === undefined) {
        this.setState({ min: minValue, max: maxValue });
      }
    });
  }
  
  handleChange(values) {
    const { filters } = this.props;
  
    const self = this;
    delayUserInput('amount', function () {
      filters.getState()
      .then(value => {
        filters.assign(self.constructor.getName(), value.set('min', values.min)
        .set('max', values.max));
      });
    }, 1000);
    
    this.setState(values);
  };
  
  
  render() {
    if (!this.state) return null;
    const { minValue, maxValue } = this.state;
    
    const min = this.state.min || minValue;
    const max = this.state.max || maxValue;
    
    return (
      <section className="field">
        <header>
          {this.getTitle()}
        </header>
        <section className="options range">
          <RCRange
            allowCross={false}
            min={minValue}
            max={maxValue}
            defaultValue={[minValue, maxValue]}
            value={[min, max]}
            onChange={([minValue, maxValue]) => this.handleChange({ min: minValue, max: maxValue })}
          />
        </section>
        
        <div className="range-inputs row">
          <div className="col-md-6">
            {this.t('general:range:min')}
            &nbsp;
            <FormattedNumberInput
              className="form-control input-sm"
              value={min}
              onChange={value => this.handleChange({ min: value, max })}
            />
          </div>
          <div className="col-md-6">
            {this.t('general:range:max')}
            &nbsp;
            <FormattedNumberInput
              className="form-control input-sm"
              value={max}
              onChange={value => this.handleChange({ min, max: value })}
            />
          </div>
        </div>
      </section>
    );
  }
}

export default FilterRange;
