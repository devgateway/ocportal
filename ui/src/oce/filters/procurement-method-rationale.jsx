import React from "react";
import MultipleSelect from './inputs/multiple-select';

const ProcurementMethodRationale = props => {
  return <MultipleSelect ep='/ocds/procurementMethodRationale/all' {...props} />
}

export default ProcurementMethodRationale;
