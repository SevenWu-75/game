package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public interface RoomHandler extends Runnable {

    void start(Room<? extends Player> room);

    void setRoom(Room<? extends Player> room);

    void signalSeatDown();

    void dismissRoom();
}
