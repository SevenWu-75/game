package com.simple.gameservice.service.impl;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.game.UserVO;
import com.simple.api.game.service.RoomManagerService;
import com.simple.api.game.service.RoomService;
import com.simple.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@DubboService
@Service
@Slf4j
public class RoomManagerServiceImpl implements RoomManagerService {

    @DubboReference
    RoomService roomService;

    private final Map<String, RoomVO<? extends Player>> roomMap = new ConcurrentHashMap<>();

    @Override
    public RoomVO<? extends Player> getRoomByRoomId(String roomId) {
        return roomMap.get(roomId);
    }

    @Override
    public RoomVO<? extends Player> getRoomByRoomIdAndGameName(String roomId, String gameName) {
        return null;
    }

    @Override
    public RoomVO<? extends Player> createRoomByGameName(String gameName, UserVO user) {
        log.trace("房间管理器尝试创建房间");
        RoomVO<? extends Player> room = roomService.createRoom(user);
        roomMap.put(room.getRoomId(), room);
        return room;
    }

    @PostConstruct
    public void cleanRoom(){
        new Thread(() -> {
            List<Room<? extends Player>> collect = roomMap.values().stream().filter(room -> room.getRoomStatus() == 3).collect(Collectors.toList());
            collect.forEach(c -> roomMap.remove(c.getRoomId()));
            try {
                Thread.sleep(1000 * 600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
