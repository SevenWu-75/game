package com.simple.speedbootdice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class SpeedBootDiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeedBootDiceApplication.class, args);
    }

}
