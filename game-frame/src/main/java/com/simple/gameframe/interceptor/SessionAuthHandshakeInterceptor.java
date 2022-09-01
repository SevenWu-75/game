package com.simple.gameframe.interceptor;

import com.simple.gameframe.common.GameException;
import com.simple.gameframe.common.GameExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
public class SessionAuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpSession session = getSession(request);
        if(session == null || session.getAttribute("user") == null || session.getAttribute("room") == null){
            log.error("websocket权限拒绝");
//            return false;
            throw new GameException(GameExceptionEnum.WEBSOCKET_TIMEOUT);
        }
        attributes.put("user",session.getAttribute("user"));
        attributes.put("room",session.getAttribute("room"));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }
}

