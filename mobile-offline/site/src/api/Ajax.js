import Axios from "axios";

export const httpGet = (url, params) => Axios.get(url, {
  responseType: "json",
  params: params,
  // withCredentials: true,  // use this param to receive/send cookies
  // credentials: "include",
  // crossDomain: true
});

export const httpPost = (url, data) => Axios.post(url, data, {
  // withCredentials: true,  // use this param to receive/send cookies
  //  credentials: "include",
  //  crossDomain: true
});
