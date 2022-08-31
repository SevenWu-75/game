package com.simple.api.game.service;

import com.simple.api.game.Room;
import com.simple.api.user.entity.User;

public interface RoomService {

    Room createRoom(User user);
}
