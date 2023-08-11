//package com.simple.web.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080","http://172.16.20.4:8080")
//                .allowedMethods("*")
//                .allowedHeaders("Origin","Content-Type","Cookie","Accept","Token")
//                .allowCredentials(true)
//                .maxAge(3600L);
//    }
//}
