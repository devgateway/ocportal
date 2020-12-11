import URI from "urijs";
import {API_ROOT} from "../state/oce-state";
import {fetchEP} from "../tools";

export const getTenders = async params => {
  const rawData = await fetch('/makueni/tenders', params);
  const data = rawData.map(datum => {
    let project,
        tender;
    if (datum.projects !== undefined && datum.projects.tenderProcesses !== undefined
        && datum.projects.tenderProcesses.tender !== undefined) {
      tender = { purchaseReqId: datum.projects.tenderProcesses._id, ...datum.projects.tenderProcesses.tender[0] };
    }
    if (datum.projects !== undefined) {
      project = {
        _id: datum.projects._id,
        projectTitle: datum.projects.projectTitle
      };
    }

    return {
      id: datum._id,
      department: datum.department.label,
      fiscalYear: datum.fiscalYear.name,
      tender: tender !== undefined ? tender : undefined,
      project: project !== undefined ? project : undefined
    };
  });

  const count = await fetch('/makueni/tendersCount', params);

  return {data, count};
}

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

export const subscribeToAlerts = params => fetch('/makueni/alerts/subscribeAlert', params);

export const fetch = (ep, params) => {
  const uri = new URI(API_ROOT + ep);
  if (params) {
    uri.addSearch(params);
  }
  return fetchEP(uri);
}
