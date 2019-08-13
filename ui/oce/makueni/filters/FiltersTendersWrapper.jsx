import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FilterItems from './FilterItems';
import FiltersWrapper from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
import FilterWards from './FilterWards';
import FilterAmount from './FilterAmount';
import FilterTitle from './FilterTitle';

/**
 * Filter used for the Tender table.
 */
class FiltersTendersWrapper extends FiltersWrapper {

}

FiltersTendersWrapper.ITEMS = [FilterTitle, FilterItemDep, FilterItemFY, FilterItems,
  FilterSubcounties, FilterWards, FilterAmount];

export default FiltersTendersWrapper;
