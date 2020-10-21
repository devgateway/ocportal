import Axios from "axios";

export const httpGet = (url, params, token) => Axios.get(url, {
  responseType: "json",
  params: params,
  headers: {
    ...headersForToken(token)
  }
})

export const httpPost = (url, data, token) => Axios.post(url, data, {
  headers: {
    ...headersForToken(token)
  }
})

const headersForToken = token => {
  if (token) {
    return { Authorization: `Bearer ${token}` }
  } else {
    return { };
  }
}
