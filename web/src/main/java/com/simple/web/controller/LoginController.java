package com.simple.web.controller;

import cn.hutool.crypto.digest.MD5;
import com.simple.api.common.Result;
import com.simple.api.common.ResultCode;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import com.simple.api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class LoginController {

    @DubboReference
    UserService userService;

    @PostMapping("/login")
    public Result<UserVO> login(HttpSession session, @RequestBody User user){
        final User one = userService.getUserByUsername(user.getUsername());
        final String password = user.getPassword();
        MD5 md5 = MD5.create();
        final String passwordEncode = md5.digestHex(password);
        if(one != null && passwordEncode.equals(one.getPassword())){
            if(!StringUtils.hasText(one.getAvatar())){
                one.setAvatar("/local/images/umr.webp");
            }
            UserVO userVO = new UserVO(one);
            session.setAttribute("user", userVO);
            log.info("用户{}登录系统",userVO.getUsername());
            return Result.success(userVO);
        }
        return Result.failed(ResultCode.LOGIN_FAILED);
    }

    @PostMapping("/register")
    public Result<User> register(HttpSession session, @RequestBody User user){
        final String username = user.getUsername();
        if(!StringUtils.hasText(username) || username.length() > 10){
            return Result.failed(ResultCode.USERNAME_INVAILD);
        }
        final String password = user.getPassword();
        if(!StringUtils.hasText(password) || password.length() > 20){
            return Result.failed(ResultCode.PASSWORD_INVAILD);
        }
        final String realname = user.getRealname();
        if(!StringUtils.hasText(realname) || realname.length() > 10){
            return Result.failed(ResultCode.REALNAME_INVAILD);
        }
        User one = userService.getUserByUsername(username);
        if(one != null){
            return Result.failed(ResultCode.ACCOUNT_EXIST);
        }
        MD5 md5 = MD5.create();
        final String passwordEncode = md5.digestHex(password);
        user.setPassword(passwordEncode);
        userService.addUser(user);
        UserVO userVO = new UserVO(user);
        session.setAttribute("user", userVO);
        return Result.success(user);
    }
}
