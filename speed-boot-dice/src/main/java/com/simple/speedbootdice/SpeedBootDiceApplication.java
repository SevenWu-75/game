package com.simple.speedbootdice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableDiscoveryClient
@EnableWebSocket
@SpringBootApplication
public class SpeedBootDiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeedBootDiceApplication.class, args);
    }

}
