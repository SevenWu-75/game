package com.simple.gameservice.service.impl;

import com.simple.api.game.Room;
import com.simple.api.game.service.RoomManagerService;
import com.simple.api.game.service.RoomService;
import com.simple.api.user.entity.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.HashMap;
import java.util.Map;

@DubboService
public class RoomManagerServiceImpl implements RoomManagerService {

    @DubboReference
    RoomService roomService;

    private final Map<String, Room> roomMap = new HashMap<>();

    @Override
    public Room getRoomByRoomId(String roomId) {
        return roomMap.get(roomId);
    }

    @Override
    public Room getRoomByRoomIdAndGameName(String roomId, String gameName) {
        return null;
    }

    @Override
    public Room createRoomByGameName(String gameName, User user) {
        Room room = roomService.createRoom(user);
        roomMap.put(room.getRoomId(), room);
        return room;
    }
}
