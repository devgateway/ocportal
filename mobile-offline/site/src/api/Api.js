import {httpPost, httpGet} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost" || document.location.protocol === "file:")
    ? "https://makuenioc.dgstg.org" : "";

const API_LOGIN = API_ROOT_URL + "/api/login";
const API_LIST_REPORTS = API_ROOT_URL + "/api/pmcReport/list/";
const API_UPDATE_REPORTS = API_ROOT_URL + "/api/pmcReport/update/";
const API_METADATA_EXPORT = API_ROOT_URL + "/api/metadataExport/";

export const loginUser = (data) => {
    return httpPost(API_LOGIN, data);
};

export const getMetadata = (userId, token) => {
    return httpGet(API_METADATA_EXPORT + userId, {}, token)
}

export const retrievePMCReports = (userId, token) => {
    return httpGet(API_LIST_REPORTS + userId, {}, token);
}

export const updatePMCReports = (userId, token, reports) => {
    return httpPost(API_UPDATE_REPORTS + userId, reports, token);
}
