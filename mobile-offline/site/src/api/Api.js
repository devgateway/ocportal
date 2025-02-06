import {httpPost, httpGet} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost" || document.location.protocol === "file:")
    ? "https://elgeyomarakwet.dgpreprod.org" : "";

const API_LOGIN = API_ROOT_URL + "/api/login";
const API_FORGOT_PASSWORD = API_ROOT_URL + "/api/user/forgotPassword";
const API_CHANGE_PASSWORD = API_ROOT_URL + "/api/user/changePassword";
const API_LIST_REPORTS = API_ROOT_URL + "/api/pmcReport/list/";
const API_UPDATE_REPORT = API_ROOT_URL + "/api/pmcReport/update/";
const API_METADATA_EXPORT = API_ROOT_URL + "/api/metadataExport/";

export const loginUser = (data) => {
    return httpPost(API_LOGIN, data);
};

export const recoverPassword = (data) => httpPost(API_FORGOT_PASSWORD, data)

export const changePassword = (data, token) => httpPost(API_CHANGE_PASSWORD, data, token)

export const getMetadata = (userId, token) => {
    return httpGet(API_METADATA_EXPORT + userId, {}, token)
}

export const retrievePMCReports = (userId, token) => {
    return httpGet(API_LIST_REPORTS + userId, {}, token);
}

export const updatePMCReport = (userId, token, reports) => httpPost(API_UPDATE_REPORT + userId, reports, token)
