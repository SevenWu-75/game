package com.simple.gameframe.interceptor;

import com.simple.api.util.ThreadLocalUtil;
import com.simple.speedbootdice.pojo.Room;
import com.simple.speedbootdice.vo.RoomVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class RoomInterceptor implements HandlerInterceptor, ExecutorChannelInterceptor {

    @Autowired
    Map<String,Room> roomMap;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object roomVo = request.getSession().getAttribute("room");
        if(roomVo != null){
            ThreadLocalUtil.setRoom(roomMap.get(String.valueOf(((RoomVo)roomVo).getRoomId())));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtil.removeRoom();
    }

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null){
            if(accessor.getSessionAttributes() != null){
                RoomVo roomVo = (RoomVo)accessor.getSessionAttributes().get("room");
                ThreadLocalUtil.setRoom(roomMap.get(roomVo.getRoomId()));
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    //可能是解散房间获取不到房间对象的情况
                    if(ThreadLocalUtil.getRoom() != null){
                        ThreadLocalUtil.getRoom().sendPublicDisconnectMessage(ThreadLocalUtil.getUser().getId());
                    }
                    log.trace("用户{}断线",ThreadLocalUtil.getUser().getId());
                }
            }
        }
        return message;
//        Object o = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//        if(o instanceof Map){
//            Map map = (Map) o;
//            if(map.containsKey("room")){
//                Object room = map.get("room");
//                List<RoomVo> roomList = JSONArray.parseArray(String.valueOf(room), RoomVo.class);
//                ThreadLocalUtil.setRoom(roomMap.get(roomList.get(0).getRoomId()));
//            }
//        }
//        return ExecutorChannelInterceptor.super.beforeHandle(message, channel, handler);
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        ThreadLocalUtil.removeRoom();
        ExecutorChannelInterceptor.super.afterMessageHandled(message, channel, handler, ex);
    }
}
