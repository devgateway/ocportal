import FilterDateYearMonth from './FilterDateYearMonth';

class FilterTenderDate extends FilterDateYearMonth {

}

FilterTenderDate.getName = () => 'Tender Close Date';
FilterTenderDate.getEP = () => '/api/tendersAwardsYears';

export default FilterTenderDate;
