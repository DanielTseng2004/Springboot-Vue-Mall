import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  register(data) {
    return apiClient.post("/users/register", data);
  },
  login(data) {
    return apiClient.post("/users/login", data);
  },
};
