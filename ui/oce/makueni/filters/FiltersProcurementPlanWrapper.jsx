import FilterItemDep from './FilterItemDep';
import FilterItemFY from './FilterItemFY';
import FiltersWrapper from './FiltersWrapper';
import fmConnect from "../../fm/fm";

/**
 * Filter used for the Procurement Plan table.
 */
class FiltersProcurementPlanWrapper extends FiltersWrapper {

}

FiltersProcurementPlanWrapper.ITEMS = [FilterItemDep, FilterItemFY];
FiltersProcurementPlanWrapper.CLASS = ['department', 'fiscal-year'];
FiltersProcurementPlanWrapper.FM = ['publicView.filter.department', 'publicView.filter.fiscalYear'];

export default fmConnect(FiltersProcurementPlanWrapper);
