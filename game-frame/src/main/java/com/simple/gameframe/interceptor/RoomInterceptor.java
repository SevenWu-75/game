package com.simple.gameframe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.user.entity.User;
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

import java.util.*;

@Component
@Slf4j
public class RoomInterceptor implements ExecutorChannelInterceptor {

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null){
            if(accessor.getSessionAttributes() != null){
                RoomVO<? extends Player> room = (RoomVO<? extends Player>)accessor.getSessionAttributes().get("room");
                 if(room == null){
                    try{
                        String roomString = (((Map<String, ArrayList>)message.getHeaders().get("nativeHeaders")).get("room").get(0)).toString();
                        room = JSONObject.parseObject(roomString, RoomVO.class);
                        accessor.getSessionAttributes().put("room",room);
                    } catch (Exception e){
                        log.error("获取用户信息失败",e);
                    }
                }
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
        first.ifPresent(player -> eventPublisher.disconnect(room, player, null));
    }
}
