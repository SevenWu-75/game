package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameException;
import com.simple.gameframe.common.GameExceptionEnum;
import com.simple.gameframe.core.ask.AskAnswerLockConditionManager;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

@Builder
public class RoundHandlerProcessor implements RoundHandler {

    LogicHandlerProcessor logicHandlerProcessor;

    Room room;

    Lock lock = new ReentrantLock();

    SeatHandler seatHandler;

    @Override
    public LogicHandlerProcessor getLogicHandlerProcessor() {
        return this.logicHandlerProcessor;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Lock getLock() {
        return this.lock;
    }

    public RoundHandlerProcessor(LogicHandlerProcessor logicHandlerProcessor, SeatHandler seatHandler){
        this.logicHandlerProcessor = logicHandlerProcessor;
        this.seatHandler = seatHandler;
    }

    @Override
    public void startLogic(@NotNull Room room) {
        final LinkedList<Player> playerList = room.getPlayerList();
        Object roundResult = null;
        for (int i = 0; i < room.getPlayCount(); i++) {
            roundResult = round(room, seatHandler.getRoundPlayer(i, playerList, roundResult));
        }
    }

    @Override
    public Object round(Room room, List<Player> players) {
        Object o = null;
        for (Player player : players) {
            o = handle(player, room, AskAnswerLockConditionManager.getLock(room.getRoomId()));
        }
        return o;
    }

    @Override
    public SeatHandler getSeatHandler() {
        return this.seatHandler;
    }

    public Object handle(Player player, Room room, Lock lock) {
        return getLogicHandlerProcessor().process(player, room, lock);
    }

    @Override
    public void run() {
        final Room currentRoom = this.room;
        this.room = null;
        lock.unlock();
        startLogic(currentRoom);
    }
}
