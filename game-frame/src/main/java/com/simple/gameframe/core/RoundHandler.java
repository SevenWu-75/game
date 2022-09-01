package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public interface RoundHandler extends Runnable {

    BiFunction<List<Player>, Integer, Player> getNextPlayerFunction();

    LogicHandlerProcessor getLogicHandlerProcessor();

    Room getRoom();

    void setRoom(Room room);

    Lock getLock();

    void startLogic(Room room);
}
