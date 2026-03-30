<template>
  <el-card
    class="styled-card"
    :body-style="{ padding: '0px' }"
    shadow="hover"
  >
    <div
      class="image-container"
      @click="$emit('view-detail', product.productId)"
    >
      <img
        :src="
          product.imageUrl ||
          'https://via.placeholder.com/300x200?text=No+Image'
        "
        class="image"
      />
      <div class="category-badge">{{ product.category }}</div>
    </div>

    <div class="card-body">
      <h3
        class="title"
        @click="$emit('view-detail', product.productId)"
      >
        {{ product.productName }}
      </h3>
      <p class="description">{{ truncate(product.description, 40) }}</p>

      <div class="price-row">
        <span class="price-symbol">$</span>
        <span class="price-value">{{ product.price.toLocaleString() }}</span>
        <span class="stock-label">庫存: {{ product.stock }}</span>
      </div>

      <div
        class="button-group"
        style="display: flex; gap: 10px; margin-top: 15px"
      >
        <el-button
          type="primary"
          plain
          @click="$emit('view-detail', product.productId)"
          >詳情</el-button
        >
        <el-button
          type="success"
          @click="handleAddToCart"
          >加入購物車</el-button
        >
      </div>
    </div>
  </el-card>
</template>

<script>
import { cartUtil } from "../api/cart";

export default {
  props: ["product"],
  methods: {
    handleAddToCart() {
      if (this.product.stock <= 0) {
        this.$message.warning("此商品暫無庫存");
        return;
      }
      cartUtil.addToCart(this.product);
      this.$message.success(`已將 ${this.product.productName} 加入購物車`);
    },
    truncate(text, length) {
      if (!text) return "";
      return text.length > length ? text.substring(0, length) + "..." : text;
    },
  },
};
</script>



