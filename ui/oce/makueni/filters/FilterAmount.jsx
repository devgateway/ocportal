import FilterRange from './FilterRange';

class FilterAmount extends FilterRange {
  componentDidMount() {
    this.setState({ minValue: 0, maxValue: 1000000000 });
    
  }
  
  getTitle() {
    return '';
  }
}

FilterAmount.getName = () => 'Amount';

export default FilterAmount;
