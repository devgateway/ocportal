import React from 'react';
import RemoteRange from './inputs/range';

const TenderPrice = ({ ep = '/tenderValueInterval', ...otherProps }) => <RemoteRange ep={ep} titleKey="filters:tenderPrice:title" {...otherProps} />;

export default TenderPrice;

export const FlaggedTenderPrice = (props) => <TenderPrice ep="/tenderValueInterval?flagged=true" {...props} />;
