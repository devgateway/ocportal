import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
// import FilterItems from './FilterItems';
import FiltersWrapper from './FiltersWrapper';
// import FilterSubcounties from './FilterSubcounties';
// import FilterWards from './FilterWards';
// import FilterAmount from './FilterAmount';
import FilterTitle from './FilterTitle';
// import FilterTenderDate from './FilterTenderDate';
import fmConnect from "../../fm/fm";
import FilterInput from "./FilterInput";

const singlePropertyRendererCreator = (FilterItem, property) => ({filters, onChange, ...props}) =>
    <FilterItem value={filters[property]} onChange={value => onChange({[property]: value})} {...props} />;

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

const titleRenderer = singlePropertyRendererCreator(FilterInput, 'text');

const FiltersTendersWrapper = props => {
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
    // {
    //   render: titleRenderer,
    //   name: 'Text Search',
    //   className: 'title-search',
    //   fm: 'publicView.filter.titleSearch'
    // }
  ];

  return <FiltersWrapper
      items={items} filters={props.filters} applyFilters={props.applyFilters} translations={props.translations} />
}

// FiltersTendersWrapper.ITEMS = [FilterTitle, FilterItemDep, FilterItemFY, FilterItems,
//   FilterSubcounties, FilterWards, FilterAmount, FilterTenderDate];
//
// FiltersTendersWrapper.CLASS = ['title-search', 'department', 'fiscal-year', 'items',
//   'subcounties', 'wards', 'amount', 'date'];
//
// FiltersTendersWrapper.FM = ['publicView.filter.titleSearch', 'publicView.filter.department',
//   'publicView.filter.fiscalYear', 'publicView.filter.items', 'publicView.filter.subcounties',
//   'publicView.filter.wards', 'publicView.filter.amount', 'publicView.filter.date'];

export default fmConnect(FiltersTendersWrapper);
