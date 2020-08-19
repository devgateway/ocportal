import {httpGet, httpPost} from "./Ajax.js";

export const API_ROOT_URL = (document.location.hostname === "localhost") ? "https://reqres.in": "";

const API_LOGIN = API_ROOT_URL + "/api/login";
const API_LOGOUT = API_ROOT_URL + "/logout";
const API_AUTHENTICATED = API_ROOT_URL + "/api/authenticated";

/**
 * Try to login the user. This API is not returning any data (for now) but is just setting the necessary cookies.
 * Check the {@link API_AUTHENTICATED} API or the {@link isAuthenticated} method
 * to see if the user is actually authenticated.
 */
export const loginUser = (data) => {
  return new Promise((resolve, reject) => {
    httpPost(API_LOGIN, data).then(resolve).catch(reject);
  });
};

/**
 * Logout an user.
 */
export const logoutUser = () => {
  return new Promise((resolve, reject) => {
    httpPost(API_LOGOUT).then(resolve).catch(reject);
  });
};

/**
 * Check if the current user is authenticated. This method should return user details.
 */
export const isAuthenticated = () => {
  return new Promise((resolve, reject) => {
    httpGet(API_AUTHENTICATED).then(resolve).catch(reject);
  });
};
