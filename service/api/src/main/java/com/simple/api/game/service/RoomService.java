package com.simple.api.game.service;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;

public interface RoomService {

    Room<? extends Player> createRoom(User user);
}
