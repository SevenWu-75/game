package com.simple.gameframe.interceptor;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RoomInterceptor implements HandlerInterceptor, ExecutorChannelInterceptor {

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null){
            if(accessor.getSessionAttributes() != null){
                Room<? extends Player> room = (Room<? extends Player>)accessor.getSessionAttributes().get("room");
                ThreadLocalUtil.setRoom(room);
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    //可能是解散房间获取不到房间对象的情况
                    if(ThreadLocalUtil.getRoom() != null){
                        sendPublicDisconnectMessage(room);
                    }
                    log.trace("用户{}断线",ThreadLocalUtil.getUser().getId());
                }
            }
        }
        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        ThreadLocalUtil.removeRoom();
        ExecutorChannelInterceptor.super.afterMessageHandled(message, channel, handler, ex);
    }

    private void sendPublicDisconnectMessage(Room<? extends Player> room){
        List<? extends Player> playerList = ThreadLocalUtil.getRoom().getPlayerList();
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        
        Optional<? extends Player> first = playerList.stream().filter(
                player -> player.getUser().getId().equals(ThreadLocalUtil.getUser().getId())).findFirst();
        if(first.isPresent()){
            eventPublisher.disconnect(room, first);
        }
    }
}
