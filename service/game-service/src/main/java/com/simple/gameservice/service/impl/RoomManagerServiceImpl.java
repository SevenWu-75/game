package com.simple.gameservice.service.impl;

import com.simple.api.game.*;
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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@DubboService
@Service
@Slf4j
public class RoomManagerServiceImpl implements RoomManagerService {

    @DubboReference(version = "1")
    RoomService speedBootRoomService;

    @DubboReference(version = "2")
    RoomService marRoomService;

    private final Map<String, RoomVO<? extends Player>> roomMap = new ConcurrentHashMap<>();

    @Override
    public RoomVO<? extends Player> getRoomByRoomId(String roomId) {
        Optional<String> first = roomMap.keySet().stream().filter(room -> room.startsWith(roomId)).findFirst();
        return first.map(roomMap::get).orElse(null);
    }

    @Override
    public RoomVO<? extends Player> getRoomByRoomIdAndGameName(String roomId, String gameName) {
        RoomVO<? extends Player> room = null;
        if("1".equals(gameName)){
            room = speedBootRoomService.getRoom(roomId);
        } else if("2".equals(gameName)){
            room = marRoomService.getRoom(roomId);
        }
        if(room != null){
            roomMap.put(room.getRoomId() + "-" + gameName, room);
        }
        return room;
    }

    @Override
    public RoomVO<? extends Player> createRoomByGameName(String gameName, UserVO user) {
        log.trace("房间管理器尝试创建房间");
        RoomVO<? extends Player> room = null;
        if("1".equals(gameName)){
            room = speedBootRoomService.createRoom(user);
        } else if("2".equals(gameName)){
            room = marRoomService.createRoom(user);
        }
        roomMap.put(room.getRoomId() + "-" + gameName, room);
        return room;
    }

    @PostConstruct
    public void cleanRoom(){
        new Thread(() -> {
            List<RoomVO<? extends Player>> collect = roomMap.values().stream().filter(room -> room.getRoomStatus() == RoomStatusEnum.over.ordinal()).collect(Collectors.toList());
            collect.forEach(c -> roomMap.remove(c.getRoomId() + "-" + c.getGameName()));
            try {
                Thread.sleep(1000 * 600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
