package com.simple.gameframe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.simple.api.game.Player;
import com.simple.api.game.RoomVO;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
public class SessionAuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpSession session = getSession(request);
        List<String> userStrings = request.getHeaders().get("user");
        List<String> roomStrings = request.getHeaders().get("room");
//        if(userStrings == null || userStrings.size() == 0 || roomStrings == null || roomStrings.size() == 0){
//            log.error("websocket权限拒绝");
////            return false;
//            throw new GameException(GameExceptionEnum.WEBSOCKET_TIMEOUT);
//        }
//        User user = JSONObject.parseObject(userStrings.get(0), User.class);
//        RoomVO<? extends Player> roomVO = JSONObject.parseObject(roomStrings.get(0), RoomVO.class);
//        session.setAttribute("user",user);
//        session.setAttribute("room",roomVO);
//        attributes.put("user",user);
//        attributes.put("room",roomVO);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.trace("");
    }
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession();
        }
        return null;
    }
}

