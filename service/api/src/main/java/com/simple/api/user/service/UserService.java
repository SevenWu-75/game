package com.simple.api.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.api.user.entity.User;

public interface UserService extends IService<User> {

    User getUserByUsername(String username);

    void addUser(User user);
}
