import FilterDateYearMonth from './FilterDateYearMonth';
import React from "react";

const FilterTenderDate = ({...otherProps}) => {
    return <FilterDateYearMonth ep='/tendersAwardsYears' {...otherProps} />
}

export default FilterTenderDate;
