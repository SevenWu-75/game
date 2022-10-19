package com.simple.web.controller;

import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.net.UnknownHostException;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({"/","/index"})
    public String index(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("user");
        if(user != null){
            if(!StringUtils.hasText(user.getAvatar())){
                user.setAvatar("/local/images/umr.webp");
            }
            session.setAttribute("user", user);
            log.info("用户{}登录系统",user.getUsername());
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }
}
