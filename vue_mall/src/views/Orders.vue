<template>
  <div class="orders-container">
    <el-card
      class="order-manage-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="header-title">{{
            userRole === "SELLER" ? "全站訂單管理" : "我的訂單"
          }}</span>
        </div>
      </template>

      <el-table
        :data="orderList"
        stripe
        v-loading="loading"
        header-cell-class-name="custom-header"
        class="styled-table"
      >
        <el-table-column
          prop="orderId"
          label="訂單編號"
          width="120"
        >
          <template #default="scope">
            <span class="order-id"># {{ scope.row.orderId }}</span>
          </template>
        </el-table-column>

        <el-table-column
          v-if="userRole === 'SELLER'"
          prop="userId"
          label="客戶 ID"
          width="100"
        >
          <template #default="scope">
            <el-tag
              size="small"
              effect="plain"
              >ID: {{ scope.row.userId }}</el-tag
            >
          </template>
        </el-table-column>

        <el-table-column
          prop="totalAmount"
          label="總金額"
          width="150"
        >
          <template #default="scope">
            <span class="amount-text"
              >${{ scope.row.totalAmount.toLocaleString() }}</span
            >
          </template>
        </el-table-column>

        <el-table-column
          prop="createdDate"
          label="成立時間"
          min-width="200"
        >
          <template #default="scope">
            <div class="time-column">
              <el-icon><Calendar /></el-icon>
              <span>{{ formatDate(scope.row.createdDate) }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          type="expand"
          label="詳情"
          width="80"
        >
          <template #default="props">
            <div class="expand-content">
              <h4>訂單商品清單</h4>
              <el-table
                :data="props.row.orderItemList"
                size="small"
                border
              >
                <el-table-column
                  prop="productName"
                  label="商品名稱"
                />
                <el-table-column
                  prop="quantity"
                  label="數量"
                  width="80"
                />
                <el-table-column
                  prop="amount"
                  label="單項小計"
                  width="120"
                />
              </el-table>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          label="操作"
          width="120"
          align="center"
        >
          <template #default="scope">
            <el-button
              type="danger"
              link
              @click="confirmDelete(scope.row.orderId)"
              class="delete-link"
            >
              <el-icon><Delete /></el-icon> 刪除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-footer">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
          background
          @current-change="fetchOrders"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Orders",
  data() {
    return {
      loading: false,
      orderList: [],
      total: 0,
      currentPage: 1,
      pageSize: 10,
      // 建議初始化時先抓一次，但 fetchOrders 內要以路由參數優先
      userRole: localStorage.getItem("userRole"),
      userId: localStorage.getItem("userId"),
    };
  },
  // 監聽路由變化：解決點擊導覽列 ID 變了但畫面沒動的問題
  watch: {
    "$route.params.userId": {
      handler() {
        this.currentPage = 1; // 切換用戶時重置頁碼
        this.fetchOrders();
      },
      immediate: false,
    },
  },
  mounted() {
    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      // 獲取當前路由上的 userId (如果有的話)
      const routeUserId = this.$route.params.userId;
      const finalUserId = routeUserId || this.userId;

      // 如果是買家且完全拿不到 ID，就不發請求
      if (this.userRole === "CUSTOMER" && !finalUserId) {
        console.warn("未偵測到 UserId，停止獲取訂單");
        return;
      }

      this.loading = true;
      try {
        const params = {
          limit: this.pageSize,
          offset: (this.currentPage - 1) * this.pageSize,
          role: this.userRole,
        };

        if (this.userRole === "CUSTOMER") {
          params.userId = finalUserId;
        }

        const response = await axios.get("/api/orders", { params });

        // 確保資料存在再賦值
        this.orderList = response.data.results || [];
        this.total = response.data.total || 0;
      } catch (error) {
        // 忽略 axios 的取消錯誤，只顯示真正的錯誤
        if (!axios.isCancel(error)) {
          this.$message.error("獲取訂單失敗");
          console.error("Fetch Orders Error:", error);
        }
      } finally {
        this.loading = false;
      }
    },
    confirmDelete(orderId) {
      // 這裡要拿正確的 userId 來刪除
      const currentUserId = this.$route.params.userId || this.userId;

      this.$confirm("確定要刪除此訂單嗎？此操作不可逆", "警告", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          try {
            // 注意：如果賣家刪除，路徑中的 userId 可能需要微調，
            // 但根據你後端 Controller，路徑確實需要一個 userId
            await axios.delete(`/api/users/${currentUserId}/orders/${orderId}`);
            this.$message.success("訂單已刪除");
            this.fetchOrders();
          } catch (error) {
            this.$message.error("刪除失敗");
          }
        })
        .catch(() => {
          // 使用者取消刪除，不執行任何動作
        });
    },
    formatDate(date) {
      if (!date) return "-";
      const d = new Date(date);
      return `${d.getFullYear()}/${(d.getMonth() + 1)
        .toString()
        .padStart(2, "0")}/${d.getDate().toString().padStart(2, "0")} ${d
        .getHours()
        .toString()
        .padStart(2, "0")}:${d.getMinutes().toString().padStart(2, "0")}`;
    },
  },
};
</script>



