package com.simple.gameframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class GameFrameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameFrameApplication.class, args);
    }

}
