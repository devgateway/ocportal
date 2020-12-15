import React from "react";
import FilterItemTypeAhead from "../makueni/filters/FilterItemTypeAhead";

const mapper = el => ({_id: el.id, label: el.name});

export const Buyer = props => {
  return <FilterItemTypeAhead
    labelKey='filters:buyer:title'
    ep='/ocds/organization/buyer/all'
    {...props} multiple={true} mapper={mapper} />;
}

export const ProcuringEntity = props => {
  return <FilterItemTypeAhead
    labelKey='filters:procuringEntity:title'
    ep='/ocds/organization/procuringEntity/all'
    {...props} multiple={true} mapper={mapper} />
}

export const Supplier = props => {
  return <FilterItemTypeAhead
    labelKey='filters:supplier:title'
    ep='/ocds/organization/supplier/all'
    {...props} multiple={true} mapper={mapper} />
}
