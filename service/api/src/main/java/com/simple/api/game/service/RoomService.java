package com.simple.api.game.service;

import com.simple.api.game.Player;
import com.simple.api.game.RoomVO;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;

public interface RoomService {

    RoomVO<? extends Player> createRoom(UserVO user);

    RoomVO<? extends Player> getRoom(String roomId);
}
