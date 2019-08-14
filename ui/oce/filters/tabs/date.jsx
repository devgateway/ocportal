import FilterDateYearMonth from '../../makueni/filters/FilterDateYearMonth';
import Tab from './index';

class FilterChartsDate extends FilterDateYearMonth {

}

FilterChartsDate.getName = () => 'Charts Date';
FilterChartsDate.getEP = () => '/api/tendersAwardsYears';

class FilterChartsTab extends Tab {
  renderChild(Component, slug) {
    const { state, onUpdate, translations } = this.props;
    
    return <Component
      translations={translations}
      onUpdate={onUpdate}
      state={state}
    />;
  }
  
  render() {
    return (
      <div>
        {this.renderChild(FilterChartsDate, 'date')}
      </div>
    );
  }
}

FilterChartsTab.getName = t => "Date";

export default FilterChartsTab;

