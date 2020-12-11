import React from "react";
import {Range} from "../../filters/inputs/range";


const FilterTenderAmount = ({...otherProps}) => {
  return <Range {...otherProps} titleKey='filters:tenderPrice:title' min={0} max={100000000}/>
}

export default FilterTenderAmount;
