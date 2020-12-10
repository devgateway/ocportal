import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
// import FilterItems from './FilterItems';
import FiltersWrapper from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
// import FilterWards from './FilterWards';
// import FilterAmount from './FilterAmount';
import FilterTenderDate from './FilterTenderDate';
import fmConnect from "../../fm/fm";
import FilterInput from "./FilterInput";
import FilterItems from "./FilterItems";
import FilterWards from "./FilterWards";

const singlePropertyRendererCreator = (FilterItem, property) => ({filters, onChange, ...props}) =>
    <FilterItem value={filters[property]}
                onChange={value => onChange({[property]: value})} {...props} />;

const dateRendererCreator = (FilterItem) => ({filters, onChange, ...props}) =>
    <FilterItem year={filters['year']} month={filters['month']}
                onChange={value => onChange(value.year.length === 1 ? {
                      year: value.year, month: value.month,
                      monthly: true
                    } :
                    {year: value.year})
                } {...props} />;

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

const titleRenderer = singlePropertyRendererCreator(FilterInput, 'text');

const itemRenderer = singlePropertyRendererCreator(FilterItems, 'item');

const tenderCloseDateRenderer = dateRendererCreator(FilterTenderDate);

const subcountiesRenderer = singlePropertyRendererCreator(FilterSubcounties, 'subcounty');

const wardsRenderer = singlePropertyRendererCreator(FilterWards, 'ward');

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
    },
    {
      render: titleRenderer,
      name: 'Text Search',
      className: 'title-search',
      fm: 'publicView.filter.titleSearch'
    },
    {
      render: itemRenderer,
      name: 'Items',
      className: 'items',
      fm: 'publicView.filter.items'
    },
    {
      render: subcountiesRenderer,
      name: 'Sub-Counties',
      className: 'subcounty',
      fm: 'publicView.filter.subcounties'
    },
    {
      render: wardsRenderer,
      name: 'Wards',
      className: 'ward',
      fm: 'publicView.filter.wards'
    },
    {
      render: tenderCloseDateRenderer,
      name: 'Tender Close Date',
      className: 'date',
      fm: 'publicView.filter.items'
    }
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
