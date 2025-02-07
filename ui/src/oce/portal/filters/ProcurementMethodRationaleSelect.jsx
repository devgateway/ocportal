import React from 'react';
import FilterItemSingleSelect from './FilterItemSingleSelect';

const ProcurementMethodRationaleSelect = (props) => (
  <FilterItemSingleSelect
    ep="/makueni/filters/procurementMethodRationale"
    itemValueKey="_id"
    itemLabelKey="label"
    {...props}
  />
);

export default ProcurementMethodRationaleSelect;
