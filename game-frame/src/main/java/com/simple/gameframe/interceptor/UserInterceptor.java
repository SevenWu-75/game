package com.simple.gameframe.interceptor;

import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class UserInterceptor implements ExecutorChannelInterceptor {

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null && accessor.getSessionAttributes() != null){
            User user = (User)accessor.getSessionAttributes().get("user");
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
