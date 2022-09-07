package com.simple.gameframe.config;

import com.simple.gameframe.core.RoomHandler;
import com.simple.gameframe.core.RoomHandlerProcessor;
import com.simple.gameframe.core.RoundHandlerProcessor;
import com.simple.gameframe.core.SeatHandlerProcessor;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;
import com.simple.gameframe.core.ask.StartLogicHandler;
import com.simple.gameframe.core.listener.DefaultEventListener;
import com.simple.gameframe.core.listener.EventListener;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.core.publisher.RoomEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableConfigurationProperties(GameFrameProperty.class)
@Configuration
public class GameFrameAutoConfiguration {

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

    @Bean
    @ConditionalOnMissingBean(RoomHandler.class)
    public RoomHandler roundHandlerProcessor(List<LogicHandler<?>> logicHandlerList){
        return new RoomHandlerProcessor(new RoundHandlerProcessor(
                new LogicHandlerProcessor(logicHandlerList),new SeatHandlerProcessor()),startLogicHandler());
    }

    @Bean
    public LogicHandler<?> startLogicHandler(){
        return new StartLogicHandler();
    }
}
