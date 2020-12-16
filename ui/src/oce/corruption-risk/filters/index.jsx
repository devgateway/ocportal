import React from "react";
import Organizations from './organizations';
import ProcurementMethodBox from './procurement-method';
import ValueAmount from './value-amount';
import DateBox from './date';
import translatable from '../../translatable';
import ProcurementMethodRationaleBox from './procurement-method-rationale';

class Filters extends translatable(React.Component) {
  render() {
    const { onUpdate, translations, currentBoxIndex, requestNewBox, state, allYears, allMonths, onApply, appliedFilters } = this.props;
    const { BOXES } = this.constructor;
    return (
      <div className="row filters-bar" onMouseDown={e => e.stopPropagation()}>
        <div className="col-md-12 crd-horizontal-filters">
        <div className="crd-filter-title">
        <div className="title">{this.t('filters:hint')}</div>
        </div>
          {BOXES.map((Box, index) => {
            return (
              <Box
                key={index}
                open={currentBoxIndex === index}
                onClick={e => requestNewBox(currentBoxIndex === index ? null : index)}
                state={state}
                onUpdate={(slug, newState) => onUpdate(state.set(slug, newState))}
                translations={translations}
                onApply={newState => onApply(newState)}
                allYears={allYears}
                allMonths={allMonths}
                appliedFilters={appliedFilters}
              />
            );
          })}
        </div>
      </div>
    );
  }
}

Filters.BOXES = [
  DateBox,
  ValueAmount,
  ProcurementMethodBox,
  ProcurementMethodRationaleBox,
  Organizations
];

export default Filters;
