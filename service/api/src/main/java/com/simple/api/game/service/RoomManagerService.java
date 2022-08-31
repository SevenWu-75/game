package com.simple.api.game.service;

import com.simple.api.game.Room;
import com.simple.api.user.entity.User;

public interface RoomManagerService {

    Room getRoomByRoomId(String roomId);

    Room getRoomByRoomIdAndGameName(String roomId, String gameName);

    Room createRoomByGameName(String gameName, User user);

}
