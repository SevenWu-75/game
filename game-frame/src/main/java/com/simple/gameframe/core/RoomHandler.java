package com.simple.gameframe.core;

import com.simple.api.game.Room;

import java.util.concurrent.CountDownLatch;

public interface RoomHandler {

    CountDownLatch getCountDownLatch(String roomId, int minPlayer);

    void cleanCountDownLatch(String roomId);

    void start(Room room);
}
