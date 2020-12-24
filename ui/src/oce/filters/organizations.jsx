import React from 'react';
import FilterItemTypeAhead from '../makueni/filters/FilterItemTypeAhead';

const mapper = (el) => ({ _id: el.id, label: el.name });

export const Buyer = (props) => (
  <FilterItemTypeAhead
    labelKey="filters:buyer:title"
    ep="/ocds/organization/buyer/all"
    {...props}
    multiple
    mapper={mapper}
  />
);

export const ProcuringEntity = (props) => (
  <FilterItemTypeAhead
    labelKey="filters:procuringEntity:title"
    ep="/ocds/organization/procuringEntity/all"
    {...props}
    multiple
    mapper={mapper}
  />
);

export const Supplier = (props) => (
  <FilterItemTypeAhead
    labelKey="filters:supplier:title"
    ep="/ocds/organization/supplier/all"
    {...props}
    multiple
    mapper={mapper}
  />
);
