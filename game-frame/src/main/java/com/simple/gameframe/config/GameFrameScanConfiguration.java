package com.simple.gameframe.config;

import com.simple.api.game.Room;
import com.simple.gameframe.util.PackageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameFrameScanConfiguration {

    @Value("${game_frame.scan}")
    String scanPackages;

    @Bean
    public Class<?> gameRoomClass(){
        Class<?> gameRoomClass = null;
        try {
            gameRoomClass = PackageUtil.findOneClassByInterface(scanPackages, Room.class);
            if(gameRoomClass == null){
                throw new ClassNotFoundException("找不到Room实现类！");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("寻找Room实现类时发生加载类失败！", e);
        }
        return gameRoomClass;
    }
}
