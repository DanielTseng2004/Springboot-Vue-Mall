// src/api/order.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
});

export default {
  // 核心方法：配合後端 @GetMapping("/orders")
  getOrders(params) {
    return apiClient.get("/orders", { params });
  },

  // 買家呼叫：傳入 userId 和 role=CUSTOMER
  getBuyerOrders(userId) {
    return apiClient.get("/orders", {
      params: { userId: userId, role: "BUYER" },
    });
  },

  // 賣家呼叫：不傳 userId，傳入 role=SELLER
  getSellerOrders(pagination = { limit: 10, offset: 0 }) {
    return this.getOrders({
      role: "SELLER",
      limit: pagination.limit,
      offset: pagination.offset,
    });
  },

  // 刪除：必須符合 /users/{userId}/orders/{orderId}
  deleteOrder(userId, orderId) {
    return apiClient.delete(`/users/${userId}/orders/${orderId}`);
  },
};
