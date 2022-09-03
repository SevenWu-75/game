package com.simple.gameframe.config;

import com.simple.gameframe.core.event.Event;
import com.simple.gameframe.core.listener.*;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.core.publisher.RoomEventPublisher;
import com.simple.gameframe.core.send.DefaultMessageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GameEventProcessorConfiguration {

    @Bean
    public DefaultEventListener defaultEventListener(){
        return new DefaultEventListener();
    }

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public RoomEventPublisher roomEventPublisher(EventListener<?>... eventListenerList){
        RoomEventPublisher roomEventPublisher = new RoomEventPublisher();
        roomEventPublisher.addListener(eventListenerList);
        return roomEventPublisher;
    }
}
