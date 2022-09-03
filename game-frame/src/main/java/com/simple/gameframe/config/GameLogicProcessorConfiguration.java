package com.simple.gameframe.config;

import com.simple.gameframe.core.*;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;
import com.simple.gameframe.core.ask.StartLogicHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GameLogicProcessorConfiguration {

    @Bean
    @ConditionalOnMissingBean(RoomHandler.class)
    public RoomHandler roundHandlerProcessor(List<LogicHandler<?>> logicHandlerList){
        return RoomHandlerProcessor.builder().roundHandler(new RoundHandlerProcessor(
                new LogicHandlerProcessor(logicHandlerList),new SeatHandlerProcessor(),new StartLogicHandler())).build();
    }
}
