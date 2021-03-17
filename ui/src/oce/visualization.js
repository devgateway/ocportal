import URI from 'urijs';
import { fromJS } from 'immutable';
import PropTypes from 'prop-types';
import Component from './pure-render-component';
import { fetchEP } from './tools';

const API_ROOT = '/api';

class Visualization extends Component {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.loading = true;
  }

  componentDidMount() {
    this.fetch();
  }

  componentDidUpdate(prevProps) {
    if (this.props.filters !== prevProps.filters) this.fetch();
  }

  getData() {
    return this.props.data;
  }

  transform(data) {
    return data;
  }

  fetch() {
    const { endpoint, endpoints } = this.constructor;
    const { requestNewData } = this.props;
    let promise = false;
    if (endpoint) {
      // console.warn('endpoint property is deprecated', endpoint);
      promise = fetchEP(this.buildUrl(endpoint));
    }
    if (endpoints) {
      // console.warn('endpoints property is deprecated', endpoints);
      promise = Promise.all(endpoints.map(this.buildUrl.bind(this))
        .map(fetchEP));
    }
    if (typeof this.getCustomEP === 'function') {
      const customEP = this.getCustomEP();
      if (Array.isArray(customEP)) {
        promise = Promise.all(customEP.map(this.buildUrl.bind(this))
          .map(fetchEP));
      } else {
        promise = fetchEP(this.buildUrl(customEP));
      }
    }
    if (!promise) return;
    this.setState({ loading: true });
    promise
      .then(this.transform.bind(this))
      .then(fromJS)
      .then((data) => requestNewData([], data))
      .then(() => this.setState({ loading: false }));
  }

  buildUrl(ep) {
    const { filters } = this.props;
    return new URI(`${API_ROOT}/${ep}`).addSearch(filters);
  }
}

Visualization.comparable = true;

Visualization.propTypes = {
  filters: PropTypes.object.isRequired,
  data: PropTypes.oneOfType([PropTypes.object, PropTypes.number, PropTypes.array]),
  requestNewData: PropTypes.func.isRequired,
};

export default Visualization;
