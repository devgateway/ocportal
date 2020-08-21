import {httpPost} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost" || document.location.protocol === "file:")
    ? "https://makuenioc.dgstg.org" : "";

const API_LOGIN = API_ROOT_URL + "/api/login";

export const loginUser = (data) => {
    return httpPost(API_LOGIN, data);
};
