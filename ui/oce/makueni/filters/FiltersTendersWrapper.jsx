import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FilterItems from './FilterItems';
import FiltersWrapper from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
import FilterWards from './FilterWards';
import FilterAmount from './FilterAmount';
import FilterTitle from './FilterTitle';
import FilterTenderDate from './FilterTenderDate';

/**
 * Filter used for the Tender table.
 */
class FiltersTendersWrapper extends FiltersWrapper {

}

FiltersTendersWrapper.ITEMS = [FilterTitle, FilterItemDep, FilterItemFY, FilterItems,
  FilterSubcounties, FilterWards, FilterAmount, FilterTenderDate];

FiltersTendersWrapper.CLASS = ['title-search', 'department', 'fiscal-year', 'items',
  'subcounties', 'wards', 'amount', 'date'];

export default FiltersTendersWrapper;
