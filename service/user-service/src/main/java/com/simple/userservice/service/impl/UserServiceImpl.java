package com.simple.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.api.user.entity.User;
import com.simple.api.user.service.UserService;
import com.simple.userservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        return this.baseMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void addUser(User user) {
        this.baseMapper.insert(user);
    }

    @Override
    public void updateUser(User user){
        this.baseMapper.updateById(user);
    }
}
