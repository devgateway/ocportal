import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper from './FiltersWrapper';

// TODO move
export const singlePropertyRendererCreator = (FilterItem, property) => ({filters, onChange, ...props}) =>
  <FilterItem value={filters[property]} onChange={value => onChange({[property]: value})} {...props} />;

// TODO move
export const minMaxPropertyRendererCreator = (FilterItem, suffix) => ({filters, onChange, ...props}) => {
  const minProperty = `min${suffix}`;
  const maxProperty = `max${suffix}`;
  return <FilterItem
    minValue={filters[minProperty]}
    maxValue={filters[maxProperty]}
    minProperty={minProperty}
    maxProperty={maxProperty}
    onChange={({minValue, maxValue}) => onChange({[minProperty]: minValue, [maxProperty]: maxValue})}
    {...props} />;
}

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

/**
 * Filter used for the Procurement Plan table.
 */
const FiltersProcurementPlanWrapper = props => {

  let groups = [
    {
      name: 'Departments',
      className: 'department',
      fm: 'publicView.filter.department',
      filters: [
        {
          render: departmentRenderer
        }
      ]
    },
    {
      name: 'Fiscal Year',
      className: 'fiscal-year',
      fm: 'publicView.filter.fiscalYear',
      filters: [
        {
          render: fyRenderer
        }
      ]
    }
  ];

  return <FiltersWrapper
    groups={groups} filters={props.filters} applyFilters={props.applyFilters} translations={props.translations} />
}

export default FiltersProcurementPlanWrapper;
