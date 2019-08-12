import translatable from '../../translatable';
import RCRange from 'rc-slider/lib/Range';
import { FormattedNumberInput } from '../../filters/inputs/range';

class FilterRange extends translatable(React.Component) {
  constructor(props) {
    super(props);

    this.handleChange = this.handleChange.bind(this);
  }
  
  handleChange(values) {
    console.log(values);
    
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
              onChange={value => this.handleChange({ min: value })}
            />
          </div>
          <div className="col-md-6">
            {this.t('general:range:max')}
            &nbsp;
            <FormattedNumberInput
              className="form-control input-sm"
              value={max}
              onChange={value => this.handleChange({ max: value })}
            />
          </div>
        </div>
      </section>
    );
  }
}

export default FilterRange;
