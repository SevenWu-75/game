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
    @ConditionalOnMissingBean(RoundHandler.class)
    public RoundHandler roundHandlerProcessor(List<LogicHandler> logicHandlerList){
        return RoundHandlerProcessor.builder()
                .logicHandlerProcessor(
                        LogicHandlerProcessor.builder()
                                .logicHandlerList(logicHandlerList).build())
                .seatHandler(new SeatHandlerProcessor())
                .startLogicHandler(new StartLogicHandler())
                .build();
    }
}
