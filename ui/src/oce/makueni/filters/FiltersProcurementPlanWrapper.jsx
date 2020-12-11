import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper, {singlePropertyRendererCreator} from './FiltersWrapper';

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
