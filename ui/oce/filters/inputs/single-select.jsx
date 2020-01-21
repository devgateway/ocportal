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

  selectAll() {
    this.props.onUpdateAll(
      Set(
        this.getOptions()
          .map(this.getId)
          .toArray(),
      ),
    );
  }

  selectElement(event) {
    if(event.target.value === "") {
      this.selectNone();
    } else {
      this.props.onUpdateAll([event.target.value]);
    }
  }

  selectNone() {
    this.props.onUpdateAll(Set());
  }

  getId(option) {
    return option.get('id');
  }

  getLabel(option) {
    return option.get('name');
  }


  render() {
    const options = this.getOptions();
    const { selected, onToggle } = this.props;
    return (
      <section className="field">
        <header>
          <header>{this.getTitle()}</header>
        </header>
        <section>
          <select placeholder="select" className="form-control" value={selected}
                  onChange={e => this.selectElement(e)}>
            <option value="">All</option>
            {options.map((option, key) => (
              <option key={this.getId(option)} value={this.getId(option)}>
                {this.getLabel(option)}
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
