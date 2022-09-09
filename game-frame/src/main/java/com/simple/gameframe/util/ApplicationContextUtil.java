package com.simple.gameframe.util;

import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.publisher.EventPublisher;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static EventPublisher eventPublisher;

    private static GameFrameProperty gameFrameProperty;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationUtil(){
        return context;
    }

    @NotNull
    public static <T> T getBean(Class<T> requiredType){
        return context.getBean(requiredType);
    }

    @NotNull
    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static SimpMessagingTemplate getSimpMessageTemplate(){
        return getBean(SimpMessagingTemplate.class);
    }

    public static EventPublisher getEventPublisher() {
        if(eventPublisher == null){
            eventPublisher = getBean(EventPublisher.class);
        }
        return eventPublisher;
    }

    public static GameFrameProperty getGameFrameProperty(){
        if(gameFrameProperty == null){
            gameFrameProperty = context.getBean(GameFrameProperty.class);
        }
        return gameFrameProperty;
    }
}
