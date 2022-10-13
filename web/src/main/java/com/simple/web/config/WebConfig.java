package com.simple.web.config;

import com.simple.web.interceptor.RoomInterceptor;
import com.simple.web.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserInterceptor userInterceptor;

    @Autowired
    RoomInterceptor roomInterceptor;

    @Value("${avatar.path}")
    String imgPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/user/*","/room/*").excludePathPatterns("/user/login");
        registry.addInterceptor(roomInterceptor).addPathPatterns("/room/*","/command/*");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + imgPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
