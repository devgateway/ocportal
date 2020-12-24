import URI from 'urijs';
import { fetchEP } from '../tools';

export const API_ROOT = '/api';

export const fetch = (ep, params) => {
  const uri = new URI(API_ROOT + ep);
  if (params) {
    uri.addSearch(params);
  }
  return fetchEP(uri);
};

export const getProject = (params) => fetch(`/makueni/project/id/${params.id}`);

export const getFeedback = (page) => fetch(`/feedback?page=${page}`);

export const postFeedback = (postData) => fetch('/postFeedback', postData);

export const getPurchaseRequisition = async (params) => {
  const rawData = await fetch(`/makueni/purchaseReq/id/${params.id}`);
  return rawData;
};

export const getTenders = async (params) => {
  const rawData = await fetch('/makueni/tenders', params);
  const data = rawData.map((datum) => {
    let project;
    let tender;
    if (datum.projects !== undefined && datum.projects.tenderProcesses !== undefined
        && datum.projects.tenderProcesses.tender !== undefined) {
      tender = { purchaseReqId: datum.projects.tenderProcesses._id, ...datum.projects.tenderProcesses.tender[0] };
    }
    if (datum.projects !== undefined) {
      project = {
        _id: datum.projects._id,
        projectTitle: datum.projects.projectTitle,
      };
    }

    return {
      id: `${datum._id}-${(tender || {})._id}-${(project || {})._id}`,
      department: datum.department.label,
      fiscalYear: datum.fiscalYear.name,
      tender: tender !== undefined ? tender : undefined,
      project: project !== undefined ? project : undefined,
    };
  });

  const count = await fetch('/makueni/tendersCount', params);

  return { data, count };
};

export const getProcurementPlans = async (params) => {
  const rawData = await fetch('/makueni/procurementPlans', params);
  const data = rawData.map((datum) => ({
    id: datum.id,
    department: datum.department.label,
    fiscalYear: datum.fiscalYear.label,
    formDocs: datum.formDocs,
  }));

  const count = await fetch('/makueni/procurementPlansCount', params);

  return { data, count };
};

export const getProcurementPlan = (id) => fetch(`/makueni/procurementPlan/id/${id}`);

export const subscribeToAlerts = (params) => fetch('/makueni/alerts/subscribeAlert', params);
