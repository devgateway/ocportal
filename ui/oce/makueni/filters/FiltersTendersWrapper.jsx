import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FilterItems from './FilterItems';
import FiltersWrapper from './FiltersWrapper';
import FilterSubcounties from './FilterSubcounties';
import FilterWards from './FilterWards';

/**
 * Filter used for the Tender table.
 */
class FiltersTendersWrapper extends FiltersWrapper {

}

FiltersTendersWrapper.ITEMS = [FilterItemDep, FilterItemFY, FilterItems, FilterSubcounties, FilterWards];

export default FiltersTendersWrapper;
