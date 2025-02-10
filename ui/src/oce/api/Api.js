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

export const getGaId = async () => {
  const gaId = await fetch('/gaId');
  return gaId;
};

export const getProject = (params) => fetch(`/client/project/id/${params.id}`);

export const getFeedback = (page) => fetch(`/feedback?page=${page}`);

export const postFeedback = (postData) => fetch('/postFeedback', postData);

export const getPurchaseRequisition = async (params) => {
  const rawData = await fetch(`/client/purchaseReq/id/${params.id}`);
  return rawData;
};

export const getTenders = async (params) => {
  const rawData = await fetch('/client/tenders', params);
  const data = rawData.map((datum) => {
    let project;
    let tender;
    if (datum.tender !== undefined) {
      tender = { purchaseReqId: datum._id, ...datum.tender };
    }

    if (datum.project !== undefined) {
      project = {
        _id: datum.project._id,
        projectTitle: datum.project.projectTitle,
      };
    }

    return {
      id: `${datum._id}-${(tender || {})._id}`,
      department: datum.department.label,
      fiscalYear: datum.fiscalYear.label,
      tender: tender !== undefined ? tender : undefined,
      project: project !== undefined ? project : undefined,
    };
  });

  const count = await fetch('/client/tendersCount', params);

  return { data, count };
};

export const getProcurementPlans = async (params) => {
  const rawData = await fetch('/client/procurementPlans', params);
  const data = rawData.map((datum) => ({
    id: datum.id,
    department: datum.department.label,
    fiscalYear: datum.fiscalYear.label,
    formDocs: datum.formDocs,
  }));

  const count = await fetch('/client/procurementPlansCount', params);

  return { data, count };
};

export const getProcurementPlan = (id) => fetch(`/client/procurementPlan/id/${id}`);

export const subscribeToAlerts = (params) => fetch('/client/alerts/subscribeAlert', params);
