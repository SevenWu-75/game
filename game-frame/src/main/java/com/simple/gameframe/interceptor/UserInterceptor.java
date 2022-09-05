package com.simple.gameframe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
@Slf4j
public class UserInterceptor implements ExecutorChannelInterceptor {

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && accessor.getSessionAttributes() != null){
            UserVO user = (UserVO)accessor.getSessionAttributes().get("user");
            if(user == null){
                try{
                    String userString = (((Map<String, ArrayList>)message.getHeaders().get("nativeHeaders")).get("user").get(0)).toString();
                    user = JSONObject.parseObject(userString, UserVO.class);
                    accessor.getSessionAttributes().put("user",user);
                } catch (Exception e){
                    log.error("获取用户信息失败",e);
                }
            }
            ThreadLocalUtil.setUser(user);
        }
        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        ThreadLocalUtil.removeUser();
        ExecutorChannelInterceptor.super.afterMessageHandled(message, channel, handler, ex);
    }
}
