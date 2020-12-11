import React from "react";
import {Range} from "../../filters/inputs/range";
import PropTypes from "prop-types";
import FilterItems from "./FilterItems";


const FilterTenderAmount = ({...otherProps}) => {
  return <Range {...otherProps} titleKey='filters:tenderPrice:title' min={0} max={100000000}/>
}

FilterItems.propTypes = {
  onChange: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired
};


export default FilterTenderAmount;
