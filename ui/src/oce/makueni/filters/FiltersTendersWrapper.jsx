import React from 'react';
import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper, { dateRendererCreator, singlePropertyRendererCreator } from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
import FilterTenderDate from './FilterTenderDate';
import fmConnect from '../../fm/fm';
import FilterInput from './FilterInput';
import FilterItems from './FilterItems';
import FilterWards from './FilterWards';
import FilterTenderAmount from './FilterTenderAmount';
import ProcurementMethodRationaleSelect from './ProcurementMethodRationaleSelect';

const wardsRendererCreator = (FilterItem) => ({ filters, onChange, ...props }) => (
  <FilterItem
    value={filters.ward}
    subcounty={filters.subcounty}
    onChange={(value) => onChange({ ward: value })}
    {...props}
  />
);

const amountRendererCreator = (FilterItem) => ({ filters, onChange, ...props }) => (
  <FilterItem
    minValue={filters.min}
    maxValue={filters.max}
    onChange={({ minValue, maxValue }) => onChange({ min: minValue, max: maxValue })}
    {...props}
  />
);

const departmentRenderer = singlePropertyRendererCreator(FilterItemDep, 'department');

const fyRenderer = singlePropertyRendererCreator(FilterItemFY, 'fiscalYear');

const procurementMethodRationaleRenderer = singlePropertyRendererCreator(
  ProcurementMethodRationaleSelect, 'procurementMethodRationale',
);

const titleRenderer = singlePropertyRendererCreator(FilterInput, 'text');

const itemRenderer = singlePropertyRendererCreator(FilterItems, 'item');

const tenderCloseDateRenderer = dateRendererCreator(FilterTenderDate);

const subcountiesRenderer = singlePropertyRendererCreator(FilterSubcounties, 'subcounty');

const wardsRenderer = wardsRendererCreator(FilterWards);

const amountsRenderer = amountRendererCreator(FilterTenderAmount);

const FiltersTendersWrapper = (props) => {
  const groups = [
    {
      name: 'Text Search',
      className: 'title-search',
      fm: 'publicView.filter.titleSearch',
      filters: [
        {
          render: titleRenderer,
        },
      ],
    },
    {
      name: 'Departments',
      className: 'department',
      fm: 'publicView.filter.department',
      filters: [
        {
          render: departmentRenderer,
        },
      ],
    },
    {
      name: 'Fiscal Year',
      className: 'fiscal-year',
      fm: 'publicView.filter.fiscalYear',
      filters: [
        {
          render: fyRenderer,
        },
      ],
    },
    {
      name: 'Procurement Method Rationale',
      className: 'fiscal-year',
      fm: 'publicView.filter.procurementMethodRationale',
      filters: [
        {
          render: procurementMethodRationaleRenderer,
        },
      ],
    },
    {
      name: 'Items',
      className: 'items',
      fm: 'publicView.filter.items',
      filters: [
        {
          render: itemRenderer,
        },
      ],
    },
    {
      name: 'Sub-Counties',
      className: 'subcounties',
      fm: 'publicView.filter.subcounties',
      filters: [
        {
          render: subcountiesRenderer,
        },
      ],
    },
    {
      name: 'Wards',
      className: 'wards',
      fm: 'publicView.filter.wards',
      filters: [
        {
          render: wardsRenderer,
        },
      ],
    },
    {
      name: 'Amounts',
      className: 'amount',
      fm: 'publicView.filter.amount',
      filters: [
        {
          render: amountsRenderer,
        },
      ],
    },
    {
      name: 'Tender Close Date',
      className: 'date',
      fm: 'publicView.filter.date',
      filters: [
        {
          render: tenderCloseDateRenderer,
        },
      ],
    },
  ];

  return (
    <FiltersWrapper
      groups={groups}
      filters={props.filters}
      applyFilters={props.applyFilters}
      translations={props.translations}
    />
  );
};

export default fmConnect(FiltersTendersWrapper);
