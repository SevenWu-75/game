package com.simple.api.game.service;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;

public interface RoomManagerService {

    Room<? extends Player> getRoomByRoomId(String roomId);

    Room<? extends Player> getRoomByRoomIdAndGameName(String roomId, String gameName);

    Room<? extends Player> createRoomByGameName(String gameName, User user);
}
