package com.simple.gameframe.config;

import com.simple.gameframe.core.listener.*;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.core.publisher.RoomEventPublisher;
import com.simple.gameframe.core.send.DefaultMessageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameEventProcessorConfiguration {

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public RoomEventPublisher roomEventPublisher(){
        RoomEventPublisher roomEventPublisher = new RoomEventPublisher();
        DefaultEventListener defaultEventListener = new DefaultEventListener();
        defaultEventListener.setMessageHandler(new DefaultMessageHandler());
        roomEventPublisher.addListener(defaultEventListener);
        return roomEventPublisher;
    }
}
