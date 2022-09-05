package com.simple.web.interceptor;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class RoomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Room<? extends Player> room = (Room<? extends Player>) request.getSession().getAttribute("room");
        if(room != null){
            ThreadLocalUtil.setRoom(room);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtil.removeRoom();
    }
}
