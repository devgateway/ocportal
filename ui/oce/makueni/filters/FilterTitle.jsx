import FilterInput from './FilterInput';

class FilterTitle extends FilterInput {
  componentDidMount() {
    super.componentDidMount();
    
  }
}

FilterTitle.getName = () => 'Text Search';

export default FilterTitle;
