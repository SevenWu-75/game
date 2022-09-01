package com.simple.gameframe.config;

import com.simple.gameframe.interceptor.RoomInterceptor;
import com.simple.gameframe.interceptor.SessionAuthHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    ChannelInterceptor userInterceptor;

    @Autowired
    RoomInterceptor roomInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //topic用来广播，user用来实现点对点
        config.enableSimpleBroker("/topic", "/user");
    }

    /**
     * 开放节点
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册两个STOMP的endpoint，分别用于广播和点对点
        //广播
        //registry.addEndpoint("/publicServer").setAllowedOriginPatterns("*").withSockJS();
        //点对点
        //registry.addEndpoint("/privateServer").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/speedBoot").setAllowedOriginPatterns("*").withSockJS()
                .setInterceptors(new SessionAuthHandshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        ChannelRegistration channelRegistration = registration.interceptors(userInterceptor, roomInterceptor);
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }


}
