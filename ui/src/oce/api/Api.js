import URI from "urijs";
import {API_ROOT} from "../state/oce-state";
import {fetchEP} from "../tools";

export const getProcurementPlans = async params => {
  const rawData = await fetch('/makueni/procurementPlans', params);
  const data = rawData.map(datum => {
    return {
      id: datum.id,
      department: datum.department.label,
      fiscalYear: datum.fiscalYear.label,
      formDocs: datum.formDocs
    };
  });

  const count = await fetch('/makueni/procurementPlansCount', params);

  return {data, count};
}

export const getProcurementPlan = id => fetch(`/makueni/procurementPlan/id/${id}`);

export const fetch = (ep, params) => {
  const uri = new URI(API_ROOT + ep);
  if (params) {
    uri.addSearch(params);
  }
  return fetchEP(uri);
}
