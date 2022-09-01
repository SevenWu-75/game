package com.simple.gameframe.config;

import com.simple.gameframe.core.*;
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
                                .logicHandlerList(logicHandlerList)
                                .waitStartLogicHandler(new WaitStartLogicHandler()).build())
                .nextPlayerFunction((players, integer) -> {
                    int size = players.size();
                    if(integer + 1 < size)
                        return players.get(integer + 1);
                    else
                        return players.get(0);
                })
                .build();
    }
}
