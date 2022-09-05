package com.simple.web.controller;

import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class IndexController {

    @GetMapping({"/","/index"})
    public String index(HttpSession session){
        UserVO user = (UserVO) session.getAttribute("user");
        if(user != null){
            session.setAttribute("user", user);
            log.info("用户{}登录系统",user.getUsername());
        }
        return "login";
    }
}
