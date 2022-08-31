package com.simple.web.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simple.api.common.Result;
import com.simple.api.common.ResultCode;
import com.simple.api.user.entity.User;
import com.simple.api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController {

    @DubboReference
    UserService userService;

    @GetMapping({"/","/index"})
    public String index(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            session.setAttribute("user", user);
            log.info("用户{}登录系统",user.getUsername());
        }
        return "login";
    }

    @PostMapping("/login")
    public Result<User> login(HttpSession session, @RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        lambdaQueryWrapper.eq(User::getPassword, user.getPassword());
        User one = userService.getOne(lambdaQueryWrapper);
        if(one != null){
            session.setAttribute("user", one);
            log.info("用户{}登录系统",one.getUsername());
            return Result.success(one);
        }
        return Result.failed(ResultCode.LOGIN_FAILED);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user){
        final String username = user.getUsername();
        if(StringUtils.hasText(username)){
            return Result.failed(ResultCode.ACCOUNT_EXIST);
        }
        final String password = user.getPassword();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User one = userService.getOne(lambdaQueryWrapper);
        if(one != null){
            return Result.failed(ResultCode.ACCOUNT_EXIST);
        }
        MD5 md5 = MD5.create();
        final String passwordEncode = md5.digestHex(password);
        user.setPassword(passwordEncode);
        userService.save(user);
        return Result.success(user);
    }
}
