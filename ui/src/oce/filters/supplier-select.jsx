import React from 'react';
import FilterItemSingleSelect from '../makueni/filters/FilterItemSingleSelect';

const SupplierSelect = (props) => (
  <FilterItemSingleSelect
    labelKey="filters:supplier:title"
    ep="/ocds/organization/supplier/all"
    itemValueKey="id"
    itemLabelKey="name"
    {...props}
  />
);

export default SupplierSelect;
