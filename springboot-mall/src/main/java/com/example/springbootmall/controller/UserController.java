package com.example.springbootmall.controller;

import com.example.springbootmall.dto.UserLoginRequest;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @CrossOrigin: 解決跨域資源共享 (CORS) 問題。
 * 允許來自 http://localhost:8081 (通常是 Vue 或 React 開發伺服器) 的請求存取此後端。
 */
@CrossOrigin(origins = "http://localhost:8081")
@RestController // 組合註解，包含 @Controller 和 @ResponseBody，確保回傳值自動轉為 JSON
public class UserController {

    @Autowired
    private UserService userService; // 注入 Service 層，負責處理具體的業務邏輯

    /**
     * 使用者註冊
     * @PostMapping: 指定處理 HTTP POST 請求
     * @RequestBody: 將前端傳來的 JSON 數據轉換為 UserRegisterRequest 物件
     * @Valid: 啟動參數校驗 (例如檢查 Email 格式、密碼長度等，需配合 DTO 裡的註解)
     */
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {

        // 1. 呼叫 Service 執行註冊，並取得資料庫生成的 userId
        Integer userId = userService.register(userRegisterRequest);

        // 2. 註冊成功後，根據 ID 查詢該使用者的完整資訊
        User user = userService.getUserById(userId);

        // 偵錯用日誌，確認前端傳過來的角色權限
        System.out.println("收到註冊角色: " + userRegisterRequest.getRole());

        // 3. 回傳 HTTP 201 Created，並在 Body 附上註冊成功的 User 物件
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * 使用者登入
     */
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {

        // 1. 呼叫 Service 進行身分驗證 (比對 Email 與 Password)
        // 注意：若登入失敗（帳密錯誤），Service 通常會拋出 400 或 403 異常
        User user = userService.login(userLoginRequest);

        // 2. 登入成功，回傳 HTTP 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}