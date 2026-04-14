package com.example.springbootmall.dao.impl;

import com.example.springbootmall.dao.UserDao;
import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;
import com.example.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用者資料存取實作類別
 *
 * @Component: 將此類別註冊為 Spring Bean，交由 Spring 容器管理
 */
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    /**
     * 創建新使用者 (註冊功能)
     *
     * @return 返回資料庫自動生成的 userId
     */
    @Override
    public Integer createUser(@NonNull UserRegisterRequest userRegisterRequest) {
        // 1. 定義 SQL 語句，使用 :name 語法作為具名參數，防止 SQL 注入
        String sql = "INSERT INTO user (email, password, role, created_date, last_modified_date) " +
                "VALUES (:email, :password, :role, :createdDate, :lastModifiedDate)";
        // 2. 將前端傳來的參數放入 Map 中，對應 SQL 中的具名參數
        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword()); // 註：實際開發建議存儲加密後的密碼
        map.put("role", userRegisterRequest.getRole());
        // 設定創建與修改時間
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);
        // 3. 使用 KeyHolder 來獲取資料庫自動生成的 Primary Key (user_id)
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // 4. 執行更新操作
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        // 5. 取得自動生成的 ID 並回傳
        int userId = keyHolder.getKey().intValue();
        return userId;
    }
    /**
     * 根據 User ID 查詢使用者資訊
     */
    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, role, created_date, last_modified_date " +
                "FROM user WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        // 使用 query 方法查詢資料，並透過 UserRowMapper 將 Resultset 轉換為 User 物件
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        // 判斷是否查詢到結果
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根據 Email 查詢使用者資訊 (常用於登入檢查或註冊重複校驗)
     */
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, role, created_date, last_modified_date " +
                "FROM user WHERE email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        // 執行查詢
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        // 檢查 email 是否存在於資料庫
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }
}