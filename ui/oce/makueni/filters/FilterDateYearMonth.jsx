import translatable from '../../translatable';
import { fetchJson, pluck, range } from '../../tools';
import cn from 'classnames';
import { Set } from 'immutable';
import { delayUserInput } from '../tenders/state';

class FilterDateYearMonth extends translatable(React.Component) {
  constructor(props) {
    super(props);
    
    this.state = {
      selectedMonths: range(1, 12),
    };
    
    this.handleChange = this.handleChange.bind(this);
  }
  
  componentDidMount() {
    this.props.filters.addListener(this.constructor.getName(), () => this.updateBindings());
    
    fetchJson('/api/tendersAwardsYears')
    .then((data) => {
      const years = data.map(pluck('_id'));
      
      this.setState({
        years,
        selectedYears: years,
      });
    });
  }
  
  componentWillUnmount() {
    this.props.filters.removeListener(this.constructor.getName());
  }
  
  updateBindings() {
    Promise.all([
      this.props.filters.getState(this.constructor.getName()),
    ])
    .then(([item]) => {
      // update internal state on reset
      const { years } = this.state;
      
      if (item.get('year') === undefined && item.get('month') === undefined) {
        this.setState({ selectedYears: years, selectedMonths: range(1, 12) });
      }
    });
  }
  
  handleChange() {
    const { filters } = this.props;
    const self = this;
    
    delayUserInput('amount', function () {
      filters.getState()
      .then(value => {
        const { selectedMonths, selectedYears } = self.state;
        
        let newValue;
        if (selectedYears.length !== 0) {
          newValue = value.set('year', Set(selectedYears));
          if (selectedYears.length === 1) {
            newValue = newValue.set('month', Set(selectedMonths));
          } else {
            newValue = newValue.delete('month');
          }
        } else {
          newValue = value.delete('year')
          .delete('month');
        }
        
        filters.assign(self.constructor.getName(), newValue);
      });
    }, 1500);
  }
  
  showMonths() {
    const { years, selectedYears } = this.state;
    
    if (years === undefined) {
      return false;
    }
    
    return selectedYears.filter(x => years.includes(x)).length === 1;
  }
  
  monthsBar() {
    const { selectedMonths } = this.state;
    return range(1, 12)
    .map(month => (<a
      key={month}
      href="javascript:void(0);"
      className={cn('col-md-3', { active: selectedMonths.includes(month) })}
      onClick={() => {
        let newSelection;
        
        if (selectedMonths.includes(month)) {
          newSelection = selectedMonths.filter(item => item !== month);
        } else {
          selectedMonths.push(month);
          newSelection = selectedMonths;
        }
        
        this.setState({
          selectedMonths: newSelection
        });
        
        this.handleChange();
      }}
    >
      <i className="glyphicon glyphicon-ok-circle"/> {this.t(`general:months:${month}`)}
    </a>));
  }
  
  yearsBar() {
    const { years, selectedYears } = this.state;
    
    const toggleYear = year => {
      let newSelection;
      if (selectedYears.includes(year)) {
        newSelection = selectedYears.filter(item => item !== year);
      } else {
        selectedYears.push(year);
        newSelection = selectedYears;
      }
      
      this.setState({
        selectedYears: newSelection
      });
      
      this.handleChange();
    };
    
    const toggleOthersYears = year => {
      this.setState({
        selectedYears: selectedYears.length === 1 && selectedYears.includes(year) ?
          years :
          [year],
      });
      
      this.handleChange();
    };
    
    return years.sort()
    .map(year =>
      (<a
        key={year}
        href="javascript:void(0);"
        className={cn('col-md-3', { active: selectedYears.includes(year) })}
        onDoubleClick={() => toggleOthersYears(year)}
        onClick={e => (e.shiftKey ? toggleOthersYears(year) : toggleYear(year))}
      >
        <i className="glyphicon glyphicon-ok-circle"/> {year}
      </a>))
    .reduce((arr, item) => {
      arr.push(item);
      return arr;
    }, []);
  }
  
  render() {
    const { years } = this.state;
    if (years === undefined) {
      return null;
    }
    
    return (<div className="date-filter">
      <div className="row years-bar" role="navigation">
        {this.yearsBar()}
      </div>
      
      <div className="row">
        <div className="hint col-md-12">
          {this.t('yearsBar:ctrlClickHint')}
        </div>
      </div>
      
      {this.showMonths() === true
        ? <div className="row months-bar" role="navigation">
          {this.monthsBar()}
        </div>
        : null
      }
    </div>);
  }
}

export default FilterDateYearMonth;
