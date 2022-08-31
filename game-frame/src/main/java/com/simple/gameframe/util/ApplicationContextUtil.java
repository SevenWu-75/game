package com.simple.gameframe.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

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

    public static MessagePublishUtil getMessagePublishUtil(){
        return getBean(MessagePublishUtil.class);
    }

    public static SimpMessagingTemplate getSimpMessageTemplate(){
        return getBean(SimpMessagingTemplate.class);
    }
}
