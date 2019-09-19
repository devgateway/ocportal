import { fromJS, Set } from 'immutable';
import translatable from '../../translatable';
import Component from '../../pure-render-component';
import { fetchJson } from '../../tools';

class SingleSelect extends translatable(Component) {
  constructor(props) {
    super(props);
    this.state = {
      options: fromJS([]),
    };
  }

  getOptions() {
    return this.state.options;
  }

  transform(datum) {
    return datum;
  }

  componentDidMount() {
    const { ENDPOINT } = this.constructor;
    if (ENDPOINT) {
      fetchJson(ENDPOINT)
      .then(data => this.setState({ options: fromJS(this.transform(data)) }));
    }
  }


  selectNone() {
    this.props.onUpdateAll(Set());
  }

  render() {
    const options = this.getOptions();
    const { selected, onToggle } = this.props;
    const totalOptions = options.count();
    return (
      <section className="field">
        <header>
          <header>{this.getTitle()}</header>
        </header>
        <section>
          <select placeholder="select" className="form-control">
          {options.map((option, key) => (
            <option key={option.get('id')} value={option.get('id')}>
              {option.get('name')}
            </option>
          ))
          .toArray()}
          </select>
        </section>
      </section>
    );
  }
}

export default SingleSelect;
