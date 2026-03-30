import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api", // 使用代理路徑
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  getProducts(params) {
    return apiClient.get("/products", { params });
  },
  getProductCategories() {
    return apiClient.get("/products/categories");
  },
  getProduct(id) {
    return apiClient.get(`/products/${id}`);
  },
  createProduct(data) {
    return apiClient.post("/products", data);
  },
  updateProduct(id, data) {
    return apiClient.put(`/products/${id}`, data);
  },
  deleteProduct(id) {
    return apiClient.delete(`/products/${id}`);
  },
};
