import URI from 'urijs';
import Visualization from '../visualization';

class PaginatedTable extends Visualization {
  constructor(...args){
    super(...args);
    this.state = this.state || {};
    this.state.pageSize = 20;
    this.state.page = 1;
  }

  getCustomEP() {
    const { pageSize, page } = this.state;
    const { dataEP, countEP, filters, years, months } = this.props;

    let data = new URI(dataEP)
      .addSearch(filters.toJS())
      .addSearch('year', years.toArray())
      .addSearch('pageSize', pageSize)
      .addSearch('month', months.toArray())
      .addSearch('pageNumber', page - 1);

    let count = new URI(countEP).addSearch(filters.toJS())
      .addSearch('year', years.toArray()).addSearch('month', months.toArray());

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
    const stateChanged = ['pageSize', 'page'].some(key => this.state[key] !== prevState[key]);
    const propsChanged = ['filters', 'years', 'months', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
    if (stateChanged || propsChanged) {
      this.fetch();
    } else {
      super.componentDidUpdate(prevProps,prevState);
    }
  }

  render() {
    throw 'Abstract!';
  }
}

export default PaginatedTable;
