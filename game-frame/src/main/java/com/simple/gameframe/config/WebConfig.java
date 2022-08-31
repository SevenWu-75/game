package com.simple.gameframe.config;

import com.simple.speedbootdice.interceptor.RoomInterceptor;
import com.simple.speedbootdice.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserInterceptor userInterceptor;

    @Autowired
    RoomInterceptor roomInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/user/*","/room/*").excludePathPatterns("/user/login");
        registry.addInterceptor(roomInterceptor).addPathPatterns("/room/*","/command/*");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
