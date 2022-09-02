package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public interface RoundHandler extends Runnable {

    LogicHandlerProcessor getLogicHandlerProcessor();

    Room getRoom();

    void setRoom(Room room);

    Lock getLock();

    void startLogic(Room room);

    Object round(Room room, List<Player> players);

    SeatHandler getSeatHandler();
}
