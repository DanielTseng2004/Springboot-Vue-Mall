package com.example.springbootmall.service.impl;

import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.UserLoginRequest;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

/**
 * 使用者業務邏輯實作類別
 *
 * @Component: 讓 Spring 管理此 Bean。在 Service 層也常使用 @Service 註解。
 */
@Component
public class UserServiceImpl implements UserService {

    // 建立日誌物件，用於記錄伺服器運行狀況（取代 System.out.println）
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    /**
     * 註冊邏輯
     */
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 1. 檢查 Email 是否已被註冊
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該 Email: {} 已被註冊", userRegisterRequest.getEmail());
            // 拋出 HTTP 400 錯誤，告知前端此請求無效
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 2. 使用 MD5 生成密碼雜湊值（Hash），確保資料庫不存儲明文密碼
        // 將前端傳來的原始密碼轉為字節後進行 MD5 加密
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        // 3. 呼叫 DAO 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    /**
     * 根據 ID 查詢使用者
     */
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    /**
     * 登入邏輯
     */
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        // 1. 根據 Email 取得使用者資訊
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        // 2. 檢查使用者是否存在
        if (user == null) {
            log.warn("該 Email: {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 3. 將前端傳入的「嘗試登入密碼」同樣進行 MD5 加密
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        // 4. 比對資料庫中的密碼（已加密）與本次加密後的結果是否一致
        if (user.getPassword().equals(hashedPassword)) {
            log.info("使用者 {} 登入成功", userLoginRequest.getEmail());
            return user;
        } else {
            log.warn("Email: {} 登入失敗，密碼錯誤", userLoginRequest.getEmail());
            // 密碼錯誤，拋出 HTTP 400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}