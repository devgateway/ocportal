import React from "react";
import RemoteRange from "./inputs/range";

const AwardValue = ({ep = '/awardValueInterval', ...otherProps}) => {
  return <RemoteRange ep={ep} titleKey='filters:awardValue:title' {...otherProps} />;
}

export default AwardValue;
