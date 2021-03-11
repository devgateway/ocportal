import URI from 'urijs';
import Visualization from '../visualization';

class PaginatedTable extends Visualization {
  constructor(...args) {
    super(...args);
    this.state = this.state || {};
    this.state.pageSize = 20;
    this.state.page = 1;
  }

  getCustomEP() {
    const { pageSize, page } = this.state;
    const {
      dataEP, countEP, filters, years, months,
    } = this.props;

    let data = new URI(dataEP)
      .addSearch(filters)
      .addSearch('pageSize', pageSize)
      .addSearch('pageNumber', page - 1);

    let count = new URI(countEP).addSearch(filters);

    if (years) {
      data = data.addSearch('year', years);
      count = count.addSearch('year', years);
    }
    if (months) {
      data.addSearch('month', months);
      count = count.addSearch('month', months);
    }

    return [
      data,
      count,
    ];
  }

  transform([data, count]) {
    return {
      data,
      count,
    };
  }

  componentDidUpdate(prevProps, prevState) {
    const stateChanged = ['pageSize', 'page'].some((key) => this.state[key] !== prevState[key]);
    const propsChanged = ['filters', 'years', 'months', 'searchQuery'].some((key) => this.props[key] !== prevProps[key]);
    if (stateChanged || propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps, prevState);
    }
  }

  render() {
    throw new Error('Abstract!');
  }
}

export default PaginatedTable;
