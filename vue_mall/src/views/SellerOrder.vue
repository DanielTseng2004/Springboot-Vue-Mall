<template>
  <el-card
    shadow="never"
    class="seller-card"
  >
    <template #header>
      <div class="header">
        <el-icon
          size="24"
          color="#409eff"
          ><List
        /></el-icon>
        <h2>銷售管理 (賣家)</h2>
      </div>
    </template>

    <el-table
      :data="orders"
      ref="ordersTable"
      style="width: 100%"
      row-key="orderId"
    >
      <el-table-column type="expand">
        <template #default="props">
          <div class="order-detail-expand">
            <h4 class="detail-title">訂單明細</h4>
            <el-table
              :data="props.row.orderItemList"
              border
              size="small"
            >
              <el-table-column
                label="商品名稱"
                prop="productName"
              />
              <el-table-column
                label="單價"
                prop="amount"
              >
                <template #default="scope">${{ scope.row.amount }}</template>
              </el-table-column>
              <el-table-column
                label="數量"
                prop="quantity"
              />
              <el-table-column label="小計">
                <template #default="scope"
                  >${{ scope.row.amount * scope.row.quantity }}</template
                >
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>

      <el-table-column
        prop="orderId"
        label="訂單編號"
        width="100"
      />

      <el-table-column
        prop="createdDate"
        label="下單時間"
      >
        <template #default="scope">
          {{ formatDate(scope.row.createdDate) }}
        </template>
      </el-table-column>

      <el-table-column
        prop="totalAmount"
        label="總金額"
      >
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: bold"
            >${{ scope.row.totalAmount }}</span
          >
        </template>
      </el-table-column>

      <el-table-column
        label="狀態"
        width="100"
      >
        <template #default="scope">
          <el-tag
            :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'"
            effect="dark"
          >
            {{ scope.row.status === "COMPLETED" ? "成功" : "進行中" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === 'PENDING'"
            size="small"
            type="success"
            @click="shipOrder(scope.row.orderId)"
          >
            確認出貨
          </el-button>

          <el-button
            size="small"
            type="primary"
            plain
            @click="toggleRow(scope.row)"
          >
            查看詳情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import { List } from "@element-plus/icons-vue";
import orderApi from "../api/order.js";

export default {
  name: "SellerOrders",
  components: { List },
  data() {
    return {
      orders: [],
    };
  },
  async mounted() {
    await this.fetchOrders();
  },
  methods: {
    // 切換展開行
    toggleRow(row) {
      this.$refs.ordersTable.toggleRowExpansion(row);
    },
    async fetchOrders() {
      const loader = this.$loadingService({ text: "載入中..." });
      try {
        const response = await orderApi.getSellerOrders();
        this.orders = response.data.results;
      } catch (error) {
        this.$message.error("獲取訂利失敗");
      } finally {
        loader.close();
      }
    },
    formatDate(dateString) {
      if (!dateString) return "";
      const date = new Date(dateString);
      return date.toLocaleString("zh-TW", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
      });
    },
    shipOrder(id) {
      this.$message.success("訂單 " + id + " 已確認出貨");
      // 這裡可以呼叫 API 更新狀態後重新 fetchOrders()
    },
  },
};
</script>

<style scoped>
.order-detail-expand {
  padding: 15px 40px;
  background-color: #f8f9fa;
}

.detail-title {
  margin-bottom: 10px;
  color: #409eff;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.header {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
