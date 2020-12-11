import React from "react";
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
// import FilterAmount from './FilterAmount';
import FilterTenderDate from './FilterTenderDate';
import fmConnect from "../../fm/fm";
import FilterInput from "./FilterInput";
import FilterItems from "./FilterItems";
import {singlePropertyRendererCreator} from "./FiltersProcurementPlanWrapper";
import FilterWards from "./FilterWards";

export const dateRendererCreator = (FilterItem) => ({filters, onChange, ...props}) =>
    <FilterItem year={filters['year']} month={filters['month']}
                onChange={value => onChange(value.year.length === 1 ? {
                  year: value.year, month: value.month,
                  monthly: true
                } : {year: value.year, month: [], monthly: false})
                } {...props} />;

const wardsRendererCreator = (FilterItem) => ({filters, onChange, ...props}) =>
    <FilterItem value={filters['ward']} subcounty={filters['subcounty']}
                onChange={value => onChange({['ward']: value})} {...props} />;

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

const titleRenderer = singlePropertyRendererCreator(FilterInput, 'text');

const itemRenderer = singlePropertyRendererCreator(FilterItems, 'item');

const tenderCloseDateRenderer = dateRendererCreator(FilterTenderDate);

const subcountiesRenderer = singlePropertyRendererCreator(FilterSubcounties, 'subcounty');

const wardsRenderer = wardsRendererCreator(FilterWards);

const FiltersTendersWrapper = props => {
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
    },
    {
      name: 'Text Search',
      className: 'title-search',
      fm: 'publicView.filter.titleSearch',
      filters: [
        {
          render: titleRenderer
        }
      ]
    },
    {
      name: 'Items',
      className: 'items',
      fm: 'publicView.filter.items',
      filters: [
        {
          render: itemRenderer
        }
      ]
    },
    {
      name: 'Sub-Counties',
      className: 'subcounties',
      fm: 'publicView.filter.subcounties',
      filters: [
        {
          render: subcountiesRenderer
        }
      ]
    },
    {
      name: 'Wards',
      className: 'wards',
      fm: 'publicView.filter.wards',
      filters: [
        {
          render: wardsRenderer
        }
      ]
    },
    {
      name: 'Tender Close Date',
      className: 'date',
      fm: 'publicView.filter.date',
      filters: [
        {
          render: tenderCloseDateRenderer
        }
      ]
    }
  ];

  return <FiltersWrapper
      groups={groups} filters={props.filters} applyFilters={props.applyFilters} translations={props.translations} />
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
