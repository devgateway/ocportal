import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper from './FiltersWrapper';

const singlePropertyRendererCreator = (FilterItem, property) => ({filters, onChange, ...props}) =>
  <FilterItem value={filters[property]} onChange={value => onChange({[property]: value})} {...props} />;

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

/**
 * Filter used for the Procurement Plan table.
 */
const FiltersProcurementPlanWrapper = props => {

  let items = [
    {
      render: departmentRenderer,
      name: 'Departments',
      className: 'department',
      fm: 'publicView.filter.department'
    },
    {
      render: fyRenderer,
      name: 'Fiscal Year',
      className: 'fiscal-year',
      fm: 'publicView.filter.fiscalYear'
    }
  ];

  return <FiltersWrapper
    items={items} filters={props.filters} applyFilters={props.applyFilters} translations={props.translations} />
}

export default FiltersProcurementPlanWrapper;
