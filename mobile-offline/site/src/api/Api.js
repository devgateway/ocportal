import {httpPost} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost") ? "http://localhost:8090" : "";

const API_LOGIN = API_ROOT_URL + "/api/login";

export const loginUser = (data) => {
    return httpPost(API_LOGIN, data);
};
