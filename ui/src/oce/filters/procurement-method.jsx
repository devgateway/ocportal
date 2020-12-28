import React from 'react';
import MultipleSelect from './inputs/multiple-select';

const ProcurementMethod = (props) => <MultipleSelect ep="/ocds/procurementMethod/all" {...props} />;

export default ProcurementMethod;
