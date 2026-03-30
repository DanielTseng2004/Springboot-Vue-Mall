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

<style scoped>
/* 保持你原本的樣式 ... */
.button-group .el-button {
  flex: 1;
}
.styled-card {
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s ease;
  height: 100%;
  border: none;
}
.styled-card:hover {
  transform: translateY(-8px);
}
.image-container {
  position: relative;
  height: 200px;
  cursor: pointer;
  overflow: hidden;
}
.image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}
.styled-card:hover .image {
  transform: scale(1.1);
}
.category-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(64, 158, 255, 0.9);
  color: white;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
}
.card-body {
  padding: 16px;
}
.title {
  margin: 0 0 8px;
  font-size: 18px;
  cursor: pointer;
}
.description {
  color: #666;
  font-size: 14px;
  height: 40px;
}
.price-row {
  display: flex;
  align-items: baseline;
  gap: 4px;
  margin-top: 12px;
}
.price-value {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
}
.stock-label {
  margin-left: auto;
  font-size: 12px;
  color: #999;
}
</style>
