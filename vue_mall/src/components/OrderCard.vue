<template>
  <el-card
    class="order-card"
    shadow="hover"
  >
    <div class="order-info">
      <h3>訂單 #{{ order.orderId }}</h3>
      <p>總價: ${{ order.totalAmount }}</p>
      <p>創建時間: {{ formatDate(order.createdDate) }}</p>
      <el-tag :type="getStatusType(order.status)">{{ order.status }}</el-tag>
    </div>
    <template #footer>
      <el-button
        type="danger"
        @click="$emit('delete-order', order.orderId)"
      >
        刪除訂單
      </el-button>
    </template>
  </el-card>
</template>

<script>
export default {
  name: "OrderCard",
  props: {
    order: {
      type: Object,
      required: true,
    },
  },
  emits: ["delete-order"],
  methods: {
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString("zh-TW");
    },
    getStatusType(status) {
      const statusMap = {
        PENDING: "warning",
        COMPLETED: "success",
        CANCELLED: "danger",
      };
      return statusMap[status] || "info";
    },
  },
};
</script>

<style scoped>
.order-card {
  margin-bottom: 20px;
}

.order-info h3 {
  margin: 0 0 10px 0;
}

.order-info p {
  margin: 5px 0;
  color: #666;
}
</style>
