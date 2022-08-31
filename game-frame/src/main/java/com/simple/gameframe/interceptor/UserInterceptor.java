package com.simple.gameframe.interceptor;

import com.simple.api.util.ThreadLocalUtil;
import com.simple.speedbootdice.vo.UserVo;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor, ExecutorChannelInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if(user == null){
            return false;
        }
        ThreadLocalUtil.setUser((UserVo) user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtil.removeUser();
    }

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && accessor.getSessionAttributes() != null){
            UserVo user = (UserVo)accessor.getSessionAttributes().get("user");
            ThreadLocalUtil.setUser(user);
        }
        return message;
//        Object o = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//        if(o instanceof Map){
//            Map map = (Map) o;
//            if(map.containsKey("Authorization")){
//                Object auth = map.get("Authorization");
//                List<UserVo> user = JSONArray.parseArray(String.valueOf(auth), UserVo.class);
//                ThreadLocalUtil.setUser(user.get(0));
//            }
//        }
//        return ExecutorChannelInterceptor.super.beforeHandle(message, channel, handler);
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        ThreadLocalUtil.removeUser();
        ExecutorChannelInterceptor.super.afterMessageHandled(message, channel, handler, ex);
    }
}
