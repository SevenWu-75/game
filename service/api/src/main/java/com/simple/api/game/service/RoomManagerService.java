package com.simple.api.game.service;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;

public interface RoomManagerService {

    RoomVO<? extends Player> getRoomByRoomId(String roomId);

    RoomVO<? extends Player> getRoomByRoomIdAndGameName(String roomId, String gameName);

    RoomVO<? extends Player> createRoomByGameName(String gameName, UserVO user);
}
