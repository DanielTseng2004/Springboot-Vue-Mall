<template>
  <div class="products-container">
    <el-card class="filter-card">
      <el-row
        :gutter="20"
        align="middle"
      >
        <el-col
          :md="8"
          :sm="12"
        >
          <el-input
            v-model="search"
            placeholder="搜尋商品名稱..."
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </el-col>

        <el-col
          :md="6"
          :sm="12"
        >
          <el-select
            v-model="selectedCategory"
            placeholder="全部分類"
            clearable
            style="width: 100%"
            @change="handleSearch"
          >
            <el-option
              v-for="item in categories"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-col>

        <el-col
          :md="4"
          :sm="24"
        >
          <el-button
            type="primary"
            @click="handleSearch"
            plain
          >
            篩選重置
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-row
      :gutter="20"
      class="product-grid"
    >
      <el-col
        v-for="item in productList"
        :key="item.productId"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        class="product-col"
      >
        <ProductCard
          :product="item"
          @view-detail="goToDetail"
        />
      </el-col>
    </el-row>

    <el-empty
      v-if="productList.length === 0"
      description="找不到符合條件的商品"
    />

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { Search } from "@element-plus/icons-vue";
import ProductCard from "../components/ProductCard.vue";
import productApi from "../api/product.js";

export default {
  name: "Products",
  components: {
    ProductCard,
    Search,
  },
  data() {
    return {
      productList: [],
      categories: [], // 儲存分類列表
      search: "", // 搜尋關鍵字
      selectedCategory: "", // 已選分類
      currentPage: 1, // 當前頁碼
      pageSize: 10, // 一頁10筆 (對應後端 limit)
      total: 0, // 總筆數 (對應後端 total)
    };
  },
  mounted() {
    this.fetchCategories();
    this.fetchAllProducts();
  },
  methods: {
    // 取得分類選單內容
    async fetchCategories() {
      try {
        const response = await productApi.getProductCategories();
        this.categories = response.data;
      } catch (error) {
        console.error("無法取得分類", error);
      }
    },
    // 取得產品列表資料
    async fetchAllProducts() {
      const loading = this.$loading({ text: "商品讀取中..." });

      // 根據後端 Controller 定義的 RequestParam 準備參數
      const params = {
        limit: this.pageSize,
        offset: (this.currentPage - 1) * this.pageSize,
        search: this.search || null,
        category: this.selectedCategory || null,
      };

      try {
        const response = await productApi.getProducts(params);
        // 後端回傳的是 Page 物件，資料在 results，總數在 total
        this.productList = response.data.results;
        this.total = response.data.total;
      } catch (error) {
        this.$message.error("獲取產品清單失敗");
        console.error(error);
      } finally {
        loading.close();
      }
    },
    // 點擊搜尋或切換分類時，強制回到第一頁
    handleSearch() {
      this.currentPage = 1;
      this.fetchAllProducts();
    },
    // 切換分頁碼
    handlePageChange(val) {
      this.currentPage = val;
      this.fetchAllProducts();
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
    goToDetail(id) {
      this.$router.push(`/products/${id}`);
    },
  },
};
</script>

<style scoped>
.filter-card {
  background: linear-gradient(135deg, #ffffff 0%, #f9fafc 100%);
  margin-bottom: 30px;
  border-radius: 12px;
  padding: 10px;
  border: 1px solid #eef1f6;
}

.product-grid {
  margin-top: 10px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  padding-left: 15px;
}

.category-select :deep(.el-input__wrapper) {
  border-radius: 20px;
}

.empty-state {
  padding: 100px 0;
}
</style>
