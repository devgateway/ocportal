import React from 'react';
import FilterItemSingleSelect from '../portal/filters/FilterItemSingleSelect';

const ProcuringEntitySelect = (props) => (
  <FilterItemSingleSelect
    labelKey="filters:procuringEntity:title"
    ep="/ocds/organization/procuringEntity/all"
    itemValueKey="id"
    itemLabelKey="name"
    {...props}
  />
);

export default ProcuringEntitySelect;
