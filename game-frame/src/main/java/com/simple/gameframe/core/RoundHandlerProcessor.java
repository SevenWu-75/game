package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class RoundHandlerProcessor implements RoundHandler {

    LogicHandlerProcessor logicHandlerProcessor;

    Room<Player> room;

    SeatHandler seatHandler;

    @Override
    public LogicHandlerProcessor getLogicHandlerProcessor() {
        return this.logicHandlerProcessor;
    }

    @Override
    public void setRoom(Room<Player> room) {
        this.room = room;
    }

    public RoundHandlerProcessor(LogicHandlerProcessor logicHandlerProcessor, SeatHandler seatHandler){
        this.logicHandlerProcessor = logicHandlerProcessor;
        this.seatHandler = seatHandler;
    }

    @Override
    public Object startLogic(@NotNull Room<Player> room) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        final List<Player> playerList = room.getPlayerList();
        Object roundResult = null;
        for (int i = 0; i < room.getPlayCount(); i++) {
            //开始回合
            eventPublisher.turnRound(room, null, i);
            roundResult = round(room, seatHandler.getRoundPlayer(i, playerList, roundResult));
        }
        return roundResult;
    }

    @Override
    public Object round(Room<Player> room, List<Player> players) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        Object o = null;
        for (Player player : players) {
            //开始换人
            eventPublisher.turnNext(room, player, players.size());
            room.setCurrentPlayer(player);
            o = handle(player, room, RoomPropertyManagerUtil.getLock(room.getRoomId()));
        }
        return o;
    }


    public Object handle(Player player, Room<Player> room, Lock lock) {
        return getLogicHandlerProcessor().handle(player, room, lock);
    }
}
