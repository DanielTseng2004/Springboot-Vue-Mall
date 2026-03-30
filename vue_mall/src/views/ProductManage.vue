<template>
  <div class="manage-container">
    <div class="header-actions">
      <el-button
        type="primary"
        @click="openDialog('create')"
      >
        <el-icon><Plus /></el-icon> 新增商品
      </el-button>
      <div class="search-bar">
        <el-input
          v-model="queryParams.search"
          placeholder="搜尋商品..."
          clearable
          @clear="fetchProducts"
          style="width: 250px"
        >
          <template #append>
            <el-button @click="fetchProducts"
              ><el-icon><Search /></el-icon
            ></el-button>
          </template>
        </el-input>
      </div>
    </div>

    <el-table
      :data="productList"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column
        prop="productId"
        label="ID"
        width="70"
      />
      <el-table-column
        label="圖片"
        width="100"
      >
        <template #default="scope">
          <el-image
            :src="scope.row.imageUrl"
            fit="cover"
            style="width: 50px; height: 50px; border-radius: 4px"
          >
            <template #error
              ><div class="image-slot">
                <el-icon><Picture /></el-icon></div
            ></template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
        prop="productName"
        label="商品名稱"
        min-width="150"
      />
      <el-table-column
        prop="category"
        label="分類"
        width="120"
      />
      <el-table-column
        prop="price"
        label="價格"
        width="100"
      >
        <template #default="scope">${{ scope.row.price }}</template>
      </el-table-column>
      <el-table-column
        prop="stock"
        label="庫存"
        width="100"
      />
      <el-table-column
        label="操作"
        width="180"
        fixed="right"
      >
        <template #default="scope">
          <el-button
            size="small"
            @click="openDialog('update', scope.row)"
            >編輯</el-button
          >
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
            >刪除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="queryParams.limit"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="handlePageChange"
      />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新增商品' : '編輯商品'"
      width="50%"
    >
      <el-form
        :model="form"
        label-width="100px"
        ref="productForm"
      >
        <el-form-item
          label="商品名稱"
          required
        >
          <el-input v-model="form.productName" />
        </el-form-item>
        <el-form-item
          label="分類"
          required
        >
          <el-select
            v-model="form.category"
            placeholder="選擇分類"
            style="width: 100%"
          >
            <el-option
              v-for="item in categories"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="圖片網址">
          <el-input
            v-model="form.imageUrl"
            placeholder="請輸入圖片連結"
          />
        </el-form-item>
        <el-form-item
          label="價格"
          required
        >
          <el-input-number
            v-model="form.price"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="庫存"
          required
        >
          <el-input-number
            v-model="form.stock"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="submitForm"
          >確認</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Search, Picture } from "@element-plus/icons-vue";
import productApi from "../api/product.js";

export default {
  name: "ProductManage",
  components: { Plus, Search, Picture },
  data() {
    return {
      loading: false,
      productList: [],
      categories: [],
      total: 0,
      currentPage: 1,
      queryParams: { limit: 10, search: "" },
      dialogVisible: false,
      dialogType: "create", // 'create' or 'update'
      form: {
        productId: null,
        productName: "",
        category: "",
        imageUrl: "",
        price: 0,
        stock: 0,
        description: "",
      },
    };
  },
  mounted() {
    this.fetchCategories();
    this.fetchProducts();
  },
  methods: {
    async fetchCategories() {
      const res = await productApi.getProductCategories();
      this.categories = res.data;
    },
    async fetchProducts() {
      this.loading = true;
      try {
        const offset = (this.currentPage - 1) * this.queryParams.limit;
        const res = await productApi.getProducts({
          ...this.queryParams,
          offset,
        });
        this.productList = res.data.results;
        this.total = res.data.total;
      } finally {
        this.loading = false;
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.fetchProducts();
    },
    openDialog(type, row = null) {
      this.dialogType = type;
      if (type === "update" && row) {
        this.form = { ...row };
      } else {
        this.form = {
          productId: null,
          productName: "",
          category: "",
          imageUrl: "",
          price: 0,
          stock: 0,
          description: "",
        };
      }
      this.dialogVisible = true;
    },
    async submitForm() {
      try {
        if (this.dialogType === "create") {
          await productApi.createProduct(this.form);
          this.$message.success("新增成功");
        } else {
          await productApi.updateProduct(this.form.productId, this.form);
          this.$message.success("更新成功");
        }
        this.dialogVisible = false;
        this.fetchProducts();
      } catch (error) {
        this.$message.error("操作失敗");
      }
    },
    handleDelete(row) {
      this.$confirm(`確定要刪除商品「${row.productName}」嗎？`, "警告", {
        type: "warning",
      })
        .then(async () => {
          await productApi.deleteProduct(row.productId);
          this.$message.success("刪除成功");
          this.fetchProducts();
        })
        .catch(() => {});
    },
  },
};
</script>



