<template>
  <div class="cart-container">
    <el-card
      class="cart-card"
      shadow="never"
    >
      <template #header>
        <div class="cart-header">
          <span class="header-title">
            <el-icon><ShoppingCart /></el-icon> 購物車清單
          </span>
          <el-button
            v-if="cartItems.length > 0"
            type="danger"
            link
            @click="clearCart"
            >清空購物車</el-button
          >
        </div>
      </template>

      <el-table
        v-if="cartItems.length > 0"
        :data="cartItems"
        style="width: 100%"
      >
        <el-table-column
          label="商品資訊"
          min-width="250"
        >
          <template #default="scope">
            <div class="product-info">
              <el-image
                :src="scope.row.imageUrl || 'https://via.placeholder.com/100'"
                class="product-img"
                fit="cover"
              />
              <div class="product-detail">
                <div class="product-name">{{ scope.row.productName }}</div>
                <div class="product-id">ID: {{ scope.row.productId }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="單價"
          width="120"
        >
          <template #default="scope"
            >${{ scope.row.price.toLocaleString() }}</template
          >
        </el-table-column>
        <el-table-column
          label="數量"
          width="180"
        >
          <template #default="scope">
            <el-input-number
              v-model="scope.row.quantity"
              :min="1"
              size="small"
              @change="updateStorage"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="小計"
          width="150"
        >
          <template #default="scope">
            <span class="subtotal-text"
              >${{
                (scope.row.price * scope.row.quantity).toLocaleString()
              }}</span
            >
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="100"
          align="center"
        >
          <template #default="scope">
            <el-button
              type="danger"
              :icon="Delete"
              circle
              plain
              @click="removeItem(scope.row.productId)"
            />
          </template>
        </el-table-column>
      </el-table>

      <el-empty
        v-else
        description="購物車是空的"
      />

      <div
        v-if="cartItems.length > 0"
        class="cart-footer"
      >
        <div class="total-section">
          <span class="total-label">總計金額：</span>
          <span class="total-amount">${{ totalAmount.toLocaleString() }}</span>
        </div>
        <div class="action-section">
          <el-button
            size="large"
            @click="$router.push('/products')"
            >繼續購物</el-button
          >
          <el-button
            type="success"
            size="large"
            :loading="loading"
            @click="handleCheckout"
            >確認結帳</el-button
          >
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ShoppingCart, Delete } from "@element-plus/icons-vue";
import { cartUtil } from "../api/cart";
import axios from "axios";

export default {
  name: "Cart",
  components: { ShoppingCart },
  data() {
    return {
      Delete,
      cartItems: [],
      loading: false,
    };
  },
  computed: {
    totalAmount() {
      return this.cartItems.reduce(
        (total, item) => total + item.price * item.quantity,
        0,
      );
    },
  },
  mounted() {
    this.loadCart();
  },
  methods: {
    loadCart() {
      this.cartItems = cartUtil.getCart();
    },
    updateStorage() {
      localStorage.setItem("cart", JSON.stringify(this.cartItems));
      window.dispatchEvent(new Event("cart-updated"));
    },
    removeItem(id) {
      cartUtil.removeFromCart(id);
      this.loadCart();
    },
    clearCart() {
      this.$confirm("確定清空嗎？", "提示", { type: "warning" })
        .then(() => {
          cartUtil.clearCart();
          this.loadCart();
        })
        .catch(() => {});
    },
    async handleCheckout() {
      const userId = localStorage.getItem("userId");
      if (!userId) return this.$router.push("/login");

      try {
        await this.$confirm(
          `總額 $${this.totalAmount.toLocaleString()}，確認購買？`,
          "結帳確認",
        );
        this.loading = true;
        const orderRequest = {
          buyItemList: this.cartItems.map((item) => ({
            productId: item.productId,
            quantity: item.quantity,
          })),
        };
        await axios.post(`/api/users/${userId}/orders`, orderRequest);
        this.$message.success("結帳成功！");
        cartUtil.clearCart();
        this.$router.push(`/orders/${userId}`);
      } catch (error) {
        if (error !== "cancel") this.$message.error("結帳失敗");
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>



