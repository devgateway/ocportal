import {httpPost} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost" || document.location.protocol === "file:")
    ? "https://makuenioc.dgstg.org" : "";

const API_LOGIN = API_ROOT_URL + "/api/login";

const dummy = true; // TODO undo!

export const loginUser = (data) => {
    if (dummy) {
        return new Promise(resolve => resolve({data: {token: "xyz"}}));
    } else {
        return httpPost(API_LOGIN, data);
    }
};
