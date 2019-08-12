import FilterRange from './FilterRange';

class FilterAmount extends FilterRange {
  componentDidMount() {
    super.componentDidMount();
    
    this.setState({ minValue: 0, maxValue: 100000000 });
    
  }
  
  getTitle() {
    return '';
  }
}

FilterAmount.getName = () => 'Amount';

export default FilterAmount;
