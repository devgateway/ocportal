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
    const { dataEP, countEP, filters } = this.props;

    let data = new URI(dataEP)
      .addSearch(filters.toJS())
      .addSearch('pageSize', pageSize)
      .addSearch('pageNumber', page - 1);

    let count = new URI(countEP).addSearch(filters.toJS());

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
    const propsChanged = ['filters', 'searchQuery'].some(key => this.props[key] !== prevProps[key]);
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
