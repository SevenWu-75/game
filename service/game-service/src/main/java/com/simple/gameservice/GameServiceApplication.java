package com.simple.gameservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.simple.gameservice.mapper")
@SpringBootApplication
public class GameServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameServiceApplication.class, args);
    }

}
