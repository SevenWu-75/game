package com.simple.gameframe.core;

import com.simple.api.game.Room;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public interface RoomHandler extends Runnable {

    void start(Room room);

    Lock getLock();

    void setRoom(Room room);
}
