# SpringBootMall（Spring Boot + Vue）

此專案為「簡易電商 / Mall」練習專案，採用 **後端 Spring Boot（REST API）** + **前端 Vue 3（Vue CLI）** 分離式架構。

---

## 專案結構

- `springboot-mall/`：後端 Spring Boot（API）
- `vue_mall/`：前端 Vue（頁面/互動）

---

## 功能概述

- **商品**
  - 商品列表（分類/關鍵字/排序/分頁）
  - 商品詳情
  - 商品新增/修改/刪除
- **會員**
  - 註冊
  - 登入
- **訂單**
  - 建立訂單
  - 訂單列表（支援分頁；可依 `userId`、`role` 查詢）
  - 刪除訂單

---

## 技術棧

### 後端（`springboot-mall`）

- Java **21**
- Spring Boot **3.5.11**
- Maven
- Spring Web / Validation / JDBC
- DB Driver：MySQL、H2（runtime）

### 前端（`vue_mall`）

- Vue **3.2.13**
- Vue Router **4.0.3**
- Vue CLI（`vue-cli-service`）
- Axios **1.13.6**
- Element Plus **2.13.6**
- Vue Loading Overlay
- Sass

---

## 環境需求

- **JDK 21+**
- **Maven 3.9+**
- **Node.js 18+**（建議）與 npm
- MySQL（若使用 `src/main/resources/application.properties` 的預設設定）

## 安全設定

為了避免將敏感資料（如資料庫密碼）提交到GitHub，請遵循以下步驟：

1. 複製 `springboot-mall/src/main/resources/application.properties.template` 到 `application.properties`
2. 編輯 `application.properties`，設定您的實際資料庫連線資訊
3. 或者設定環境變數：
   - `DB_HOST`：資料庫主機
   - `DB_PORT`：資料庫埠號
   - `DB_NAME`：資料庫名稱
   - `DB_USERNAME`：資料庫使用者名稱
   - `DB_PASSWORD`：資料庫密碼

**注意**：`application.properties` 已被加入 `.gitignore`，不會被提交到倉庫。

---

## 快速開始（本機開發）

### 1) 啟動後端 API（Spring Boot）

在 `springboot-mall/` 目錄：

```powershell
mvn -v
mvn spring-boot:run
```

後端預設提供於 `http://localhost:8080`（若你有自行改 port，請以實際設定為準）。

---

### 2) 啟動前端（Vue）

在 `vue_mall/` 目錄：

```powershell
npm install
npm run serve
```

---

## 前後端連線與 Port（重要）

### 代理（Proxy）

前端 `vue_mall/vue.config.js` 有設定 devServer proxy：

- 當你呼叫 `/api/...` 時，會被代理到 `http://localhost:8080`，並把 `/api` 前綴移除（`pathRewrite`）。

### CORS

後端 Controller 目前設定：

- `@CrossOrigin(origins = "http://localhost:8081")`

因此若你的前端實際跑在 `http://localhost:8080`（Vue CLI 預設），會出現 **CORS** 問題。

你可以擇一處理：

- **方式 A（推薦，最少改動）**：讓前端跑在 **8081**
  - Windows PowerShell：

```powershell
$env:PORT=8081
npm run serve
```

- **方式 B**：把後端 CORS 的 `origins` 改成你的前端實際網址（例如 `http://localhost:8080`）
- **方式 C**：改成更通用的 CORS 設定（例如允許多個 origins / 使用全域 CORS 設定）

### Axios baseURL（提醒）

前端 `src/api/http.js` 會讀取 `VUE_APP_API_BASE_URL` 來當作 `baseURL`。目前專案的 `vue_mall/.env` 只有 `VUE_APP_API=/api`，因此：

- 若 `VUE_APP_API_BASE_URL` 未設定，API 會走相對路徑（例如 `/products`），不會自動加 `/api` 前綴。
- 若你希望前端所有 API 都走 proxy（`/api`），可新增或調整環境變數：
  - 在 `vue_mall/.env` 增加：
    - `VUE_APP_API_BASE_URL=/api`

---

## 資料庫設定

### MySQL（預設：main）

後端設定檔：`springboot-mall/src/main/resources/application.properties`

- **URL**：`jdbc:mysql://localhost:3306/mall?serverTimezone=Asia/Taipei&characterEncoding=utf-8`
- **username**：`root`
- **password**：`springboot`

請先建立資料庫：

```sql
CREATE DATABASE mall;
```

> 註：此專案目前未在 `src/main/resources` 提供自動初始化用的 `schema.sql`/`data.sql`，若你要用 MySQL 直接跑起來，需自行建立資料表與測試資料（可參考下方「H2 測試資料」的 SQL）。

## 主要 API（後端）

以下路徑依 Controller 實作為準（後端沒有 `/api` 前綴；`/api` 是前端 proxy 用的）。

### 商品 Products（`ProductController`）

- `GET /products`
  - query：`category`、`search`、`orderBy`、`sort`、`limit`、`offset`
- `GET /products/{productId}`
- `POST /products`
- `PUT /products/{productId}`
- `DELETE /products/{productId}`

### 會員 Users（`UserController`）

- `POST /users/register`
- `POST /users/login`

### 訂單 Orders（`OrderController`）

- `GET /orders`
  - query：`userId`（可選）、`limit`、`offset`、`role`（`CUSTOMER`/`SELLER`）
- `POST /users/{userId}/orders`
- `DELETE /users/{userId}/orders/{orderId}`

---

## 前端頁面（Vue Router）

主要路由（`vue_mall/src/router/index.js`）：

- `/`：首頁
- `/products`：商品列表
- `/users/register`：註冊
- `/users/login`：登入
- `/users/cart`：購物車
- `/users/:userId/orders`：使用者訂單
- `/seller/orderreceived`：賣家訂單頁（UI）
- `/manager`：商品管理（UI）

---

## 後端詳細資訊（`springboot-mall`）

一個簡單的 Spring Boot Mall 範例專案（Spring Boot 3 / Java 21 / Maven）。

### 功能概述

- 提供基本的 Web API（`spring-boot-starter-web`）
- 參數驗證（`spring-boot-starter-validation`）
- 資料庫存取（JDBC）
- 內建可選資料庫：H2（開發/測試）或 MySQL（部署/實際環境）

### 專案啟動（PowerShell）

在 `springboot-mall/` 目錄執行：

```powershell
mvn -v
mvn spring-boot:run
```

或先打包再執行：

```powershell
mvn clean package
java -jar .\target\springboot-mall-0.0.1-SNAPSHOT.jar
```

---

## 前端詳細資訊（`vue_mall`）

這是一個簡單的 Vue.js 練習專案，範例包含商品列表、購物車、訂單流程、會員登入註冊，以及簡易後台商品管理介面。

### 🧩 功能

- 商品列表與詳細資訊
- 購物車管理
- 結帳流程與訂單紀錄
- 會員登入與註冊
- 管理員商品新增/編輯
- API 請求封裝於 `src/api/*`

### 🚀 快速開始

#### 1. 安裝相依套件

```bash
npm install
```

#### 2. 開啟開發伺服器

```bash
npm run serve
```

然後在瀏覽器打開：http://localhost:8080

#### 3. 打包（正式環境）

```bash
npm run build
```

#### 4. 程式碼格式檢查與修正

```bash
npm run lint
```

### 📁 專案結構

- `src/main.js` - Vue 應用入口
- `src/App.vue` - 根元件
- `src/router/index.js` - Vue Router 設定
- `src/views/` - 各頁面（Home、Cart、Login、等）
- `src/components/` - 可重用元件
- `src/api/` - API 請求模組（product、order、user）
- `src/assets/` - 樣式與靜態資源
