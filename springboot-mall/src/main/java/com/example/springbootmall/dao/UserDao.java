package com.example.springbootmall.dao;

import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.User;

import java.util.List;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);

}
