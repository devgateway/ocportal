import Organizations from './organizations';
import ProcurementMethodBox from './procurement-method';
import ValueAmount from './value-amount';
import DateBox from './date';
import translatable from '../../translatable';

class Filters extends translatable(React.Component) {
  render() {
    const { onUpdate, translations, currentBoxIndex, requestNewBox, state, allYears, allMonths, onApply, appliedFilters } = this.props;
    const { BOXES } = this.constructor;
    return (
      <div className="row filters-bar" onMouseDown={e => e.stopPropagation()}>
        <div className="col-md-3 crd-filter-title">
        <div className="title">{this.t('filters:hint')}</div>
        </div>
        <div className="col-md-9 crd-horizontal-filters">
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
  Organizations
];

export default Filters;
