<template>
  <div class="product-detail">
    <el-row
      v-if="product"
      justify="center"
    >
      <el-col :span="20">
        <el-card
          shadow="hover"
          class="detail-card"
        >
          <template #header>
            <div class="product-header">
              <h2>{{ product.productName }}</h2>
              <el-tag
                type="success"
                size="large"
                >${{ product.price }}</el-tag
              >
            </div>
          </template>

          <el-row :gutter="40">
            <el-col
              :md="10"
              :sm="24"
            >
              <div class="product-image-container">
                <el-image
                  :src="product.imageUrl"
                  :preview-src-list="[product.imageUrl]"
                  fit="contain"
                  class="product-main-image"
                >
                  <template #error>
                    <div class="image-slot">
                      <el-icon><Picture /></el-icon>
                      <span>暫無圖片</span>
                    </div>
                  </template>
                </el-image>
              </div>
            </el-col>

            <el-col
              :md="14"
              :sm="24"
            >
              <el-descriptions
                :column="1"
                border
                class="product-info-table"
              >
                <el-descriptions-item label="產品名稱">{{
                  product.productName
                }}</el-descriptions-item>
                <el-descriptions-item label="商品類別">
                  <el-tag size="small">{{ product.category }}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="當前庫存">
                  <el-tag :type="product.stock > 0 ? 'success' : 'danger'">
                    {{ product.stock > 0 ? product.stock : "缺貨" }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="上架日期">{{
                  formatDate(product.createdDate)
                }}</el-descriptions-item>
              </el-descriptions>

              <div class="product-description">
                <h3>產品描述</h3>
                <p class="desc-text">{{ product.description }}</p>
              </div>

              <div class="action-buttons">
                <el-button
                  @click="$router.go(-1)"
                  size="large"
                >
                  <el-icon><ArrowLeft /></el-icon> 返回列表
                </el-button>

                <el-button
                  type="primary"
                  size="large"
                  :disabled="product.stock <= 0"
                  class="buy-btn"
                  @click="handleAddToCart"
                >
                  <el-icon><ShoppingCart /></el-icon> 加入購物車
                </el-button>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row
      v-else
      justify="center"
    >
      <el-col :span="12">
        <el-card>
          <el-skeleton
            :rows="10"
            animated
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ArrowLeft, ShoppingCart, Picture } from "@element-plus/icons-vue";
import productApi from "../api/product.js";
// 修正點 2：匯入購物車工具類 (請確保路徑正確)
import { cartUtil } from "../api/cart.js";

export default {
  name: "ProductDetail",
  components: {
    ArrowLeft,
    ShoppingCart,
    Picture,
  },
  data() {
    return {
      product: null,
    };
  },
  async mounted() {
    // 這裡的參數名稱應與路由定義一致，通常是 id 或 productId
    const id = this.$route.params.productId || this.$route.params.id;
    if (id) {
      await this.fetchProduct(id);
    }
  },
  methods: {
    // 修正點 3：實作加入購物車方法
    handleAddToCart() {
      if (this.product) {
        cartUtil.addToCart(this.product);
        this.$message.success(`已將 ${this.product.productName} 加入購物車`);
      }
    },
    async fetchProduct(id) {
      const loading = this.$loading({
        lock: true,
        text: "載入中...",
        background: "rgba(0, 0, 0, 0.7)",
      });
      try {
        const response = await productApi.getProduct(id);
        this.product = response.data;
      } catch (error) {
        this.$message.error("獲取產品詳情失敗");
      } finally {
        loading.close();
      }
    },
    formatDate(dateString) {
      if (!dateString) return "";
      return new Date(dateString).toLocaleDateString("zh-TW");
    },
  },
};
</script>



