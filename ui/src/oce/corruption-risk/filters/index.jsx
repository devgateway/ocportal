import React from "react";
import Organizations from './organizations';
import ProcurementMethodBox from './procurement-method';
import ValueAmount from './value-amount';
import DateBox from './date';
import {tCreator} from '../../translatable';
import ProcurementMethodRationaleBox from './procurement-method-rationale';

const BOXES = [
  DateBox,
  ValueAmount,
  ProcurementMethodBox,
  ProcurementMethodRationaleBox,
  Organizations
];

const Filters = props => {
  const { onUpdate, translations, currentBoxIndex, requestNewBox, state, allYears, allMonths, onApply, appliedFilters } = props;

  const t = tCreator(props.translations);

  return (
    <div className="row filters-bar" onMouseDown={e => e.stopPropagation()}>
      <div className="col-md-12 crd-horizontal-filters">
        <div className="crd-filter-title">
          <div className="title">{t('filters:hint')}</div>
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

export default Filters;
