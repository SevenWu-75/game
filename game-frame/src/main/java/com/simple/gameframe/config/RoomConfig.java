package com.simple.gameframe.config;

import com.simple.speedbootdice.pojo.Room;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RoomConfig {

    @Bean
    public Map<String, Room> roomMap(){
        return new HashMap<>();
    }
}
