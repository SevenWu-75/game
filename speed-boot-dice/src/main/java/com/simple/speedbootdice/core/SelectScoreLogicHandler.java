package com.simple.speedbootdice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.LogicHandler;
import com.simple.gameframe.core.Message;

import java.util.concurrent.ConcurrentHashMap;

public class SelectScoreLogicHandler implements LogicHandler {

    private ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    private LogicHandler nextHandler;

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return null;
    }

    @Override
    public Message<?> messageHandle(Player player, Room room, Object o) {
        return null;
    }

    @Override
    public void setNextHandler(LogicHandler logicHandler) {
        this.nextHandler = logicHandler;
    }

    @Override
    public LogicHandler getNextHandler() {
        return this.nextHandler;
    }
}
