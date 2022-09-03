package com.simple.api.user.service;


import com.simple.api.user.entity.User;

public interface UserService {

    User getUserByUsername(String username);

    void addUser(User user);
}
