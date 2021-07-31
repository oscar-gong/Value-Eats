import { BACKEND_PORT } from "./constants";
class Request {
  constructor () {
    this.url = `${BACKEND_PORT}`;
  }

  getHeaders (token) {
    const fetchHeaders = {
      "Content-Type": "application/json",
      Accept: "application/json"
    };
    if (token !== null) fetchHeaders.Authorization = `${token}`;
    return fetchHeaders;
  }

  post (path, payload, token = null) {
    return fetch(`${this.url}/${path}`, {
      method: "POST",
      headers: this.getHeaders(token),
      body: JSON.stringify(payload)
    });
  }

  get (path, token = null) {
    return fetch(`${this.url}/${path}`, {
      method: "GET",
      headers: this.getHeaders(token)
    });
  }

  put (path, payload, token = null) {
    return fetch(`${this.url}/${path}`, {
      method: "PUT",
      headers: this.getHeaders(token),
      body: JSON.stringify(payload)
    });
  }

  delete (path, payload, token = null) {
    return fetch(`${this.url}/${path}`, {
      method: "DELETE",
      headers: this.getHeaders(token),
      body: JSON.stringify(payload)
    });
  }
}

const request = new Request();

export default request;
