import React from 'react';
import FilterItemSingleSelect from '../makueni/filters/FilterItemSingleSelect';

const BuyerSelect = (props) => (
  <FilterItemSingleSelect
    labelKey="filters:buyer:title"
    ep="/ocds/organization/buyer/all"
    itemValueKey="id"
    itemLabelKey="name"
    {...props}
  />
);

export default BuyerSelect;
