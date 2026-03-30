<template>
  <el-card shadow="never">
    <template #header>
      <div class="orders-header">
        <el-icon
          size="24"
          color="#409eff"
          ><List
        /></el-icon>
        <h2>我的訂單 (買家)</h2>
      </div>
    </template>

    <el-table
      :data="orders"
      ref="buyerTable"
      style="width: 100%"
      row-key="orderId"
    >
      <el-table-column type="expand">
        <template #default="props">
          <div class="order-detail-expand">
            <h4 class="detail-title">商品明細</h4>
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
      <el-table-column label="下單時間">
        <template #default="scope">{{
          formatDate(scope.row.createdDate)
        }}</template>
      </el-table-column>
      <el-table-column
        label="金額"
        width="180"
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
            size="small"
            type="primary"
            plain
            @click="toggleRow(scope.row)"
            >查看詳情</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row.orderId)"
            >刪除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-empty
      v-if="orders.length === 0"
      description="暫無訂單"
    />
  </el-card>
</template>

<script>
import { List } from "@element-plus/icons-vue"; // 記得補上 Icon 引入
import orderApi from "../api/order.js";

export default {
  components: { List },
  data() {
    return { orders: [] };
  },
  async mounted() {
    // 統一呼叫 fetchOrders，不要在 mounted 寫重複邏輯
    await this.fetchOrders();
  },
  methods: {
    toggleRow(row) {
      this.$refs.buyerTable.toggleRowExpansion(row);
    },
    async fetchOrders() {
      const loader = this.$loadingService({ text: "載入中..." });
      try {
        const userId = localStorage.getItem("userId");

        if (!userId) {
          this.$message.warning("偵測不到用戶 ID，請重新登入");
          return;
        }

        const response = await orderApi.getBuyerOrders(userId);
        this.orders = response.data.results || [];

        if (this.orders.length === 0) {
          console.log("後端回傳成功，但該用戶目前沒有任何訂單紀錄");
        }
      } catch (error) {
        this.$message.error("獲取訂單失敗");
      } finally {
        loader.close();
      }
    },
    async handleDelete(orderId) {
      try {
        await this.$confirm("確定要刪除嗎？", "提示", { type: "warning" });
        const userId = localStorage.getItem("userId");
        await orderApi.deleteOrder(userId, orderId);
        this.$message.success("訂單已刪除");
        this.fetchOrders();
      } catch (e) {
        if (e !== "cancel") this.$message.error("刪除失敗");
      }
    },
    formatDate(dateString) {
      if (!dateString) return "";
      return new Date(dateString).toLocaleString("zh-TW");
    },
  },
};
</script>



