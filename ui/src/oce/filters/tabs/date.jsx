import FilterDateYearMonth from '../../makueni/filters/FilterDateYearMonth';
import Tab from './index';
import React from "react";

//TODO: do we still need this ?
const FilterChartsDate = ({ep, ...otherProps}) => {
  return <FilterDateYearMonth ep='/api/tendersAwardsYears' {...otherProps} />
}

class FilterChartsTab extends Tab {
  renderChild(Component, slug) {
    const {state, onUpdate, translations} = this.props;

    return <Component
        translations={translations}
        onUpdate={onUpdate}
        state={state}
    />;
  }
  
  render() {
    return (
      <div>
        {/*{this.renderChild(FilterChartsDate, 'date')}*/}
      </div>
    );
  }
}

FilterChartsTab.getName = t => "Date";

export default FilterChartsTab;

