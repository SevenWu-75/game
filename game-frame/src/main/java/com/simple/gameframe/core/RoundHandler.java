package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public interface RoundHandler {

    LogicHandlerProcessor getLogicHandlerProcessor();
    void setRoom(Room<Player> room);

    Object startLogic(Room<Player> room);

    Object round(Room<Player> room, List<Player> players);
}
